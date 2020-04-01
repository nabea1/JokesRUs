package com.galvanize.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.entities.Category;
import com.galvanize.entities.Joke;
import com.galvanize.repositories.JokeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class JokeControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    JokeController controller;
    @MockBean
    JokeRepository jokeRepository;
    ObjectMapper mapper = new ObjectMapper();
    List<Joke> testJokes = new ArrayList<>();
    @BeforeEach
    void setUp() {
        assertNotNull(mvc);
        assertNotNull(controller);
        //Generate Test Data
        for (int i = 1; i <=10; i++) {
            if(i %2 == 0){
                testJokes.add(new Joke((long) (i * 1000), Category.DADJOKES, "How do you make holy water? You boil the hell out of it."+i));
            }else{
                testJokes.add(new Joke((long) (i * 2000), Category.TECHNOLOGY, "There's no place like 127.0.0.1"+i));
            }
        }
    }
    //    POST: new joke - accept any joke in one of the specified categories
    @Test
    void createNewJoke() throws Exception {
        Joke newJoke = new Joke(Category.MOMJOKES, "I bought some shoes from a drug dealer. I don't know what he laced them with, but I was tripping all day!");
        newJoke.setId(1L);
        String jokeJson = mapper.writeValueAsString(newJoke);
        when(jokeRepository.save(ArgumentMatchers.any(Joke.class))).thenReturn(newJoke);
        mvc.perform(post("/api/jokes").contentType(MediaType.APPLICATION_JSON).content(jokeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jokeId").exists());
    }
    //    GET: All Jokes in DB
    @Test
    void getAllJokes() throws Exception {
        when(jokeRepository.findAll()).thenReturn(testJokes);
        mvc.perform(get("/api/jokes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }
    //    GET: All Jokes containing certain search string
    @Test
    void searchJokesByString() throws Exception {
        String searchString = "technology joke";
        when(jokeRepository.findAllByJokeContains(searchString))
                .thenReturn(testJokes.stream().filter(j -> j.getJoke().contains(searchString)).collect(Collectors.toList()));
        mvc.perform(get("/api/jokes/search").param("searchString", searchString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
    //    GET: All Jokes by Category
    @Test
    void getJokesByCategory() throws Exception {
        when(jokeRepository.findAllByCategory(Category.DADJOKES))
                .thenReturn(testJokes.stream().filter(j -> j.getCategory().equals(Category.DADJOKES)).collect(Collectors.toList()));
        mvc.perform(get("/api/jokes/category/DADJOKES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
    //    GET: Random Joke by Optional Category
    @Test
    void getRandomJoke_withCategory() throws Exception {
        when(jokeRepository.findRandomJokeByCategory(testJokes.get(2).getCategory().toString())).thenReturn(testJokes.get(2));
        mvc.perform(get("/api/jokes/random").param("category", testJokes.get(2).getCategory().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category", is(testJokes.get(2).getCategory().toString())));
    }
    //    PATCH: Update any of the fields for a Joke
    @Test
    void getRandomJoke_noCategory() throws Exception {
        when(jokeRepository.findRandomJokeByCategory(anyString())).thenReturn(testJokes.get(2));
        mvc.perform(get("/api/jokes/random"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category", is(testJokes.get(2).getCategory().toString())));
    }

    //    DELETE: A Joke by Id
    @Test
    void deleteJokeById_notExists() throws Exception {
        when(jokeRepository.existsById(anyLong())).thenReturn(false);
        mvc.perform(delete("/api/jokes/1000"))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("errorMsg"));
    }
    @Test
    void deleteJokeById_exists() throws Exception {
        when(jokeRepository.existsById(1000L)).thenReturn(true);
        mvc.perform(delete("/api/jokes/1000"))
                .andExpect(status().isOk());
    }
}
