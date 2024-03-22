package server.yousong.models;
import org.springframework.data.rest.core.config.Projection;
import java.util.Set;

@Projection(name = "kompakt", types = Song.class)
public interface SongProjection {
    String getTitle();
    Set<String> getGenres();
    String getLength();
    Artist getArtist();
    long getVersion();

}
