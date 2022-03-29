package tr.com.readingisgood.app.service.genre;

import org.springframework.stereotype.Service;
import tr.com.readingisgood.app.repository.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

}
