package server.yousong.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;
    @NotBlank(message = "Das Lied muss einen Titel haben")
    private String title;
  //  @NotBlank(message = "Das Lied muss einen Künstler haben")

    @ManyToOne
    private Artist artist;

    @ElementCollection
    @NotEmpty
    private Set<String> genres;

    @Lob
    private String data;

    //@Pattern(regexp = "[\\d]+[:][\\d]+")
    private String length;
}