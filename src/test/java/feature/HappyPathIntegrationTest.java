package feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.songify.SongifyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    public void should_complete_song_happy_path() throws Exception {
        // step 1: there are no songs
        mockMvc.perform(get("/songs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", empty()));

        // step 2: add a song and read its generated id
        MvcResult createSongResult = mockMvc.perform(post("/songs")
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

        // step 3: the song can be retrieved from the database
        mockMvc.perform(get("/songs/{id}", songId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) songId)))
                .andExpect(jsonPath("$.name", is("Till I Collapse")));

        // step 4: update the song
        mockMvc.perform(put("/songs/{id}", songId)
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

        // step 5: the updated song is visible on the list
        mockMvc.perform(get("/songs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(1)))
                .andExpect(jsonPath("$.songs[0].id", is((int) songId)))
                .andExpect(jsonPath("$.songs[0].name", is("Lose Yourself")));

        // step 6: delete the song together with its genre
        mockMvc.perform(delete("/songs/{id}/genre", songId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Deleted song with id: " + songId)))
                .andExpect(jsonPath("$.status", is("OK")));

        // step 7: the list is empty again
        mockMvc.perform(get("/songs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", empty()));
    }
}
