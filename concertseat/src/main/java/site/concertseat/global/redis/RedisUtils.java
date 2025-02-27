package site.concertseat.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtils{

    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setDataWithExpiration(String key, Object value, Long expiredTime) {
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
