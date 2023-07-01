package com.example.spring_data_redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;


    public User getUser(Long id) {
        var key = "users:%d".formatted(id);
        var cacheUser = objectRedisTemplate.opsForValue().get(key);
        if (cacheUser != null) {
            return (User) cacheUser;
        }
        User user = userRepository.findById(id).orElseThrow();
        objectRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(5));
        return user;
    }


}
