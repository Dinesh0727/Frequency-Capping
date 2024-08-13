package com.dinnaop.learning_redis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@RedisHash("EndUser")
@Data
public class EndUser {
    @Id
    private String esmeAddress;

    private String five_second_frequency;

    private String five_second_oldest_timestamp;

    private String one_minute_frequency;

    private String one_minute_oldest_timestamp;

}
