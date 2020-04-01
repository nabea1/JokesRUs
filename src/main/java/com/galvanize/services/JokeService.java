package com.galvanize.services;
import com.galvanize.exceptions.RecordNotFoundException;
import com.galvanize.entities.Category;
import com.galvanize.entities.Joke;
import com.galvanize.repositories.JokeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class JokeService {
    JokeRepository jokeRepository;
    public JokeService(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }
    public List<Joke> getAllJokes() {
        return jokeRepository.findAll();
    }
    public List<Joke> findJokeContaining(String searchString) {
        return jokeRepository.findAllByJokeContains(searchString);
    }
    public List<Joke> findJokesByCategory(Category category) {
        return jokeRepository.findAllByCategory(category);
    }

        public Joke getRandomJoke(Category category) {
            return jokeRepository.findRandomJoke();
        }
        public Joke getRandomeJokeByCategory(Category category){
            String catString = category == Category.NA ? "%" : category.toString();
            return jokeRepository.findRandomJokeByCategory(catString);
        }
        public void deleteById(long id) {
            if(jokeRepository.existsById(id)) {
                jokeRepository.deleteById(id);
            }else{
                throw new RecordNotFoundException("Joke number "+id+" was not found" );
            }
        }
        public Joke addJoke(Joke joke) {
            return jokeRepository.save(joke);
        }
    }
