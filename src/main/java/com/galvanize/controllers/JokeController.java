package com.galvanize.controllers;

import com.galvanize.exceptions.RecordNotFoundException;
import com.galvanize.entities.Category;
import com.galvanize.entities.Joke;
import com.galvanize.services.JokeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jokes")
public class JokeController {

    JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @PostMapping
    public Joke createJoke(@RequestBody Joke joke){
        return jokeService.addJoke(joke);
    }

    @GetMapping
    public List<Joke> getAllJokes(){
        return jokeService.getAllJokes();
    }

    @GetMapping("/search")
    public List<Joke> getJokesContaining(@RequestParam String searchString){
        return jokeService.findJokeContaining(searchString);
    }

    @GetMapping("/category/{category}")
    public List<Joke> getJokesByCategory(@PathVariable Category category){
        return jokeService.findJokesByCategory(category);
    }

    @GetMapping("/random")
    public ResponseEntity<Joke> getRandomJoke(@RequestParam(required = false, defaultValue = "%") String category){
        Joke randomJoke;
        if(category.equals("%")){
            randomJoke = jokeService.getRandomeJokeByCategory(Category.NA);
            return ResponseEntity.ok(randomJoke);
        }else{
            Category cat = Category.valueOf(category);
            if (cat != null) {
                randomJoke = jokeService.getRandomeJokeByCategory(Category.valueOf(category));
                return ResponseEntity.ok(randomJoke);
            }else{
                return ResponseEntity.notFound().header("errorMsg", "Category "+category+" is not valid").build();
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJokeById(@PathVariable Long id){
        try{
            jokeService.deleteById(id);
            return ResponseEntity.ok("Deleted joke number "+id);
        }catch (RecordNotFoundException e){
            return ResponseEntity.noContent().header("errorMsg", e.getMessage()).build();
        }
    }
}
