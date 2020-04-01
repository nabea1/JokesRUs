package com.galvanize.services;

import com.galvanize.entities.Category;
import com.galvanize.entities.Joke;
import com.galvanize.repositories.JokeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JokeServiceTest {
    @Qualifier("jokeRepository")
    @Autowired
    private JokeRepository jokeRepository;

    @Test
    void getAllJokes() {
        Joke joke = new Joke(Category.TECHNOLOGY, "Roses are red, violets are blue, unexpected '{' on line 32");
        jokeRepository.save(joke);
        JokeService jokeService = new JokeService(jokeRepository);

        Joke firstResult = jokeService.getAllJokes().get(0);

        assertEquals(joke.getJoke(), firstResult.getJoke());
        assertEquals(joke.getCategory(), firstResult.getCategory());
        assertEquals(joke.getId(), firstResult.getId());
    }

}
