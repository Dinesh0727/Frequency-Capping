package com.dinnaop.learning_redis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dinnaop.learning_redis.model.Book;
import com.dinnaop.learning_redis.model.EndUser;
import com.dinnaop.learning_redis.producer.MessageProducer;
import com.dinnaop.learning_redis.repository.BookRepository;
import com.dinnaop.learning_redis.repository.EndUserRepository;

@RestController
public class Controller {

    private final BookRepository repo;

    private final EndUserRepository userRepo;

    @Autowired
    private MessageProducer producer;

    public Controller(BookRepository repo, EndUserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @PostMapping("/addBook")
    public String addBook(@RequestBody Book entity) {
        repo.save(entity);
        return entity.toString();
    }

    @PostMapping("/addListOfBooks")
    public String addListOfBooks(@RequestBody List<Book> entities) {
        repo.saveAll(entities);
        return "All ok as all the books are added";
    }

    @GetMapping("/getBooks")
    public ArrayList<Book> getAllBooks() {
        return (ArrayList<Book>) repo.findAll();
    }

    @GetMapping("/trigger")
    public String trigger() throws Exception {

        ArrayList<Book> books = (ArrayList<Book>) repo.findAll();
        Book tempBook = books.get(0);
        int i = 0;
        while (i < 10) {
            producer.sendMessage(tempBook);
            Thread.sleep(2000);
            i++;
        }

        return "Triggered";
    }

    @GetMapping("/getEndUser")
    public EndUser getEndUser() {
        ArrayList<EndUser> users = (ArrayList<EndUser>) userRepo.findAll();
        return users.get(0);
    }

    @PostMapping("/addEndUser")
    public String addEndUser(@RequestBody EndUser user) {
        user.setFive_second_frequency("0");
        user.setOne_minute_frequency("0");
        userRepo.save(user);

        return "Added the end user : " + user.toString();
    }
}