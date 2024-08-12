package com.dinnaop.learning_redis.repository;

import org.springframework.data.repository.CrudRepository;

import com.dinnaop.learning_redis.model.Book;

public interface BookRepository extends CrudRepository<Book, String> {

}
