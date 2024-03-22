package server.yousong.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import server.yousong.models.Song;
import server.yousong.models.SongProjection;

@CrossOrigin
@RepositoryRestResource(path = "songs", collectionResourceRel = "songs", excerptProjection = SongProjection.class)
public interface SongRepository
        extends PagingAndSortingRepository<Song, Long> {
    Page<Song> findByArtistContainingIgnoreCase(@Param("artist") String a, Pageable p);
    Page<Song> findByTitleContainingIgnoreCase(@Param("title") String t, Pageable p);
}