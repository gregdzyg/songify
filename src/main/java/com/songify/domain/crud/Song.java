package com.songify.domain.crud;
import com.songify.domain.crud.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor

@Table(name = "song")
class Song extends BaseEntity {

    @Id
    @GeneratedValue(generator = "song_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "song_id_seq", sequenceName = "song_id_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    String name;
    String artist;
    private Instant releaseDate;
    private Long duration;
    @Enumerated(EnumType.STRING)
    private SongLanguage language;
    @OneToOne(optional = false)
    private Genre genre;
    @ManyToOne
    private Album album;

    public Song() {
    }

    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public Song(Long id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }

}
