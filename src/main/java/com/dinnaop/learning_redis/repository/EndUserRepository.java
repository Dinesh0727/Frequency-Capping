package com.dinnaop.learning_redis.repository;

import org.springframework.data.repository.CrudRepository;

import com.dinnaop.learning_redis.model.EndUser;

public interface EndUserRepository extends CrudRepository<EndUser, String> {

}
