package com.dinnaop.learning_redis.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Book")
public class Book implements Serializable {
    private String author;
    private String title;
    @Id
    private String score;

    public Book(String author, String title, String score) {
        this.author = author;
        this.title = title;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Book [author=" + author + ", title=" + title + ", score=" + score + "]";
    }

    public Book() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
