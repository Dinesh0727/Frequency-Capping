package com.dinnaop.learning_redis.consumer;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dinnaop.learning_redis.model.Book;
import com.dinnaop.learning_redis.model.EndUser;
import com.dinnaop.learning_redis.repository.EndUserRepository;

@Service
public class MessageConsumer {

    @Value("${customer.endUser.five_second_frequency}")
    private int five_second_freq_cap;

    @Value("${customer.endUser.one_minute_frequency}")
    private int one_minute_freq_cap;

    @Autowired
    private EndUserRepository repo;

    @RabbitListener(queues = { "${rabbitmq.hash.queue.name}" })
    public void consumeMessage(Book book) {
        ArrayList<EndUser> users = (ArrayList<EndUser>) repo.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }
        EndUser user = users.get(0);

        Long currentTime = System.currentTimeMillis();
        String fiveSecondOldestTimestamp = user.getFive_second_oldest_timestamp();
        String oneMinuteOldestTimestamp = user.getOne_minute_oldest_timestamp();

        if (fiveSecondOldestTimestamp == null || oneMinuteOldestTimestamp == null) {
            user.setFive_second_frequency("1");
            user.setOne_minute_frequency("1");
            user.setFive_second_oldest_timestamp(currentTime.toString());
            user.setOne_minute_oldest_timestamp(currentTime.toString());
            repo.save(user);
            System.out.println("First message consumed and timestamps set.");
            return;
        }

        if (check(user.getFive_second_frequency(), fiveSecondOldestTimestamp,
                user.getOne_minute_frequency(), oneMinuteOldestTimestamp, currentTime)) {

            user.setFive_second_oldest_timestamp(currentTime.toString());
            int fiveSecondFrequency = Integer.parseInt(user.getFive_second_frequency());
            if (fiveSecondFrequency >= five_second_freq_cap) {
                user.setFive_second_frequency("1");
                user.setFive_second_oldest_timestamp(currentTime.toString());
            } else {
                user.setFive_second_frequency(String.valueOf(fiveSecondFrequency + 1));
            }

            int oneMinuteFrequency = Integer.parseInt(user.getOne_minute_frequency());
            if (oneMinuteFrequency >= one_minute_freq_cap) {
                user.setOne_minute_frequency("1");
                user.setOne_minute_oldest_timestamp(currentTime.toString());
            } else {
                user.setOne_minute_frequency(String.valueOf(oneMinuteFrequency + 1));
            }

            repo.save(user);
            System.out.println("Message Consumed : at " + LocalDateTime.now());
        } else {
            System.out.println("Dropped Silently");
        }
    }

    private boolean check(String five_second_frequency, String five_second_oldest_timestamp,
            String one_minute_frequency, String one_minute_oldest_timestamp, long present_time) {

        long oldFiveSecondTime = Long.parseLong(five_second_oldest_timestamp);
        long oldOneMinuteTime = Long.parseLong(one_minute_oldest_timestamp);
        int fiveSecondFreq = Integer.parseInt(five_second_frequency);
        int oneMinuteFreq = Integer.parseInt(one_minute_frequency);

        boolean fiveSecondCheck = (present_time - oldFiveSecondTime > 5000) || (fiveSecondFreq < five_second_freq_cap);
        boolean oneMinuteCheck = (present_time - oldOneMinuteTime > 60000) || (oneMinuteFreq < one_minute_freq_cap);

        return fiveSecondCheck && oneMinuteCheck;
    }
}
