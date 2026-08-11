package feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.songify.SongifyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SongifyApplication.class)
@Testcontainers
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class HappyPathIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("songify.security.jwt-secret",
                () -> "integration-test-secret-with-more-than-32-characters");
    }

    @Test
    public void should_complete_song_happy_path() throws Exception {
        // step 1: protected endpoints reject anonymous requests
        mockMvc.perform(get("/songs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // step 2: a new account can be registered and receives the USER role
        mockMvc.perform(post("/auth/register")
                        .content("""
                                {
                                  "username": "listener",
                                  "password": "listener-password"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("listener")))
                .andExpect(jsonPath("$.role", is("USER")));

        mockMvc.perform(post("/auth/register")
                        .content("""
                                {
                                  "username": " Listener ",
                                  "password": "another-password"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Username 'listener' is already taken")));

        mockMvc.perform(post("/auth/token")
                        .content("""
                                {
                                  "username": "listener",
                                  "password": "wrong-password"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid username or password")));

        String userToken = fetchToken("listener", "listener-password");

        // step 3: USER can read the catalogue, but cannot modify it
        mockMvc.perform(get("/songs")
                        .header(HttpHeaders.AUTHORIZATION, bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", empty()));

        mockMvc.perform(post("/songs")
                        .header(HttpHeaders.AUTHORIZATION, bearer(userToken))
                        .content("""
                                {
                                  "name": "Till I Collapse",
                                  "releaseDate": "2002-05-26T00:00:00Z",
                                  "duration": 297,
                                  "language": "ENGLISH"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // Integration setup creates an ADMIN without exposing an admin registration endpoint
        jdbcTemplate.update("""
                        INSERT INTO application_user(username, password_hash, role, enabled)
                        VALUES (?, ?, 'ADMIN', TRUE)
                        """,
                "admin",
                passwordEncoder.encode("admin-password"));
        String adminToken = fetchToken("admin", "admin-password");

        // step 4: ADMIN can add a song and read its generated id
        MvcResult createSongResult = mockMvc.perform(post("/songs")
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .content("""
                                {
                                  "name": "Till I Collapse",
                                  "releaseDate": "2002-05-26T00:00:00Z",
                                  "duration": 297,
                                  "language": "ENGLISH"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Till I Collapse")))
                .andReturn();

        long songId = objectMapper.readTree(createSongResult.getResponse().getContentAsString())
                .get("id")
                .asLong();

        // step 5: the song can be retrieved from the database
        mockMvc.perform(get("/songs/{id}", songId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) songId)))
                .andExpect(jsonPath("$.name", is("Till I Collapse")));

        // step 6: update the song
        mockMvc.perform(put("/songs/{id}", songId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .content("""
                                {
                                  "name": "Lose Yourself",
                                  "artist": "Eminem"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) songId)))
                .andExpect(jsonPath("$.name", is("Lose Yourself")));

        // step 7: the updated song is visible on the list
        mockMvc.perform(get("/songs")
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(1)))
                .andExpect(jsonPath("$.songs[0].id", is((int) songId)))
                .andExpect(jsonPath("$.songs[0].name", is("Lose Yourself")));

        // step 8: delete the song together with its genre
        mockMvc.perform(delete("/songs/{id}/genre", songId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Deleted song with id: " + songId)))
                .andExpect(jsonPath("$.status", is("OK")));

        // step 9: the list is empty again
        mockMvc.perform(get("/songs")
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", empty()));
    }

    private String fetchToken(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/token")
                        .content("""
                                {
                                  "username": "%s",
                                  "password": "%s"
                                }
                                """.formatted(username, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andExpect(jsonPath("$.expiresIn", is(3600)))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accessToken")
                .asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
