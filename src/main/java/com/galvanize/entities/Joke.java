package com.galvanize.entities;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Locale;

@Entity
public class Joke {
    @Id
    private long id;
    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column
    private String joke;

    public Joke() {
    }

    public Joke(long id, Category category, String joke) {
        this.id = id;
        this.category = category;
        this.joke = joke;
    }

    public Joke(Category category, String joke) {
        this.category = category;
        this.joke = joke;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }
}
