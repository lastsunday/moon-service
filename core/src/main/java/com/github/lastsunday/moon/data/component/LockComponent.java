package com.github.lastsunday.moon.data.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.lastsunday.moon.constant.DataKey.LOCK_KEY_PREFIX;

@Component
public class LockComponent {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取乐观锁
     *
     * @param key
     * @param timeoutSec 锁过期时间
     * @return
     */
    public boolean tryLock(String key, Integer timeoutSec) {
        return redisTemplate.opsForValue().setIfAbsent(LOCK_KEY_PREFIX + key, System.currentTimeMillis() + "",
                Duration.ofSeconds(timeoutSec));
    }

    public boolean tryLockMulti(Collection<String> keys, Integer timeoutSec) {
        Map<String, String> map = new HashMap<>();
        String now = System.currentTimeMillis() + "";
        for (String key : keys) {
            map.put(key, now);
        }
        boolean suc = redisTemplate.opsForValue().multiSetIfAbsent(map);
        if (suc) {
            keys.forEach(item -> {
                redisTemplate.expire(item, timeoutSec, TimeUnit.SECONDS);
            });
        }
        return suc;
    }

    public void release(String key) {
        redisTemplate.delete(LOCK_KEY_PREFIX + key);
    }

    public boolean hashPut(String table, String key) {
        return redisTemplate.opsForHash().putIfAbsent(table, key, key);
    }

    public boolean hashContains(String table, String key) {
        return redisTemplate.opsForHash().hasKey(table, key);
    }

    public void hashDel(String table, String key) {
        redisTemplate.opsForHash().delete(table, key);
    }
}
