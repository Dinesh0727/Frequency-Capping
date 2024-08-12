package com.dinnaop.learning_redis.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dinnaop.learning_redis.model.Book;
import com.dinnaop.learning_redis.repository.BookRepository;

@RestController
public class Controller {

    private BookRepository repo;

    public Controller(BookRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/addBook")
    public String addBook(@RequestBody Book entity) {
        repo.save(entity);
        return entity.toString();
    }
}
