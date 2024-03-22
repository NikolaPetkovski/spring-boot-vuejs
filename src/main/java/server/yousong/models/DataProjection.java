package server.yousong.models;

import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "data", types = Song.class)
public interface DataProjection {

    String getData();


}
