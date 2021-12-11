package com.example.nationalpetition.service.redis;

import com.example.nationalpetition.dto.notification.response.NotificationInfoResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    String topic = "socket.io";
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(NotificationInfoResponse notification) {
        log.info("redis publish");
        Gson gson = new Gson();
        redisTemplate.convertAndSend(topic, gson.toJson(notification));
    }

}
