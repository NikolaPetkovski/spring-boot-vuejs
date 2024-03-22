package server.yousong.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import server.yousong.models.Artist;

@CrossOrigin
@RepositoryRestResource(path = "artist", collectionResourceRel = "artist")
public interface ArtistRepository
        extends PagingAndSortingRepository<Artist, Long> {
}