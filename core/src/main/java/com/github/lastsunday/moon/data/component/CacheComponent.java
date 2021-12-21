package com.github.lastsunday.moon.data.component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

@Component
public class CacheComponent {

	@Autowired
	private StringRedisTemplate redisTemplate;

	public void putObj(String key, Object obj, Integer expireSec) {
		if (expireSec != null) {
			redisTemplate.opsForValue().set(key, JSONObject.toJSONString(obj), expireSec, TimeUnit.SECONDS);
		} else {
			redisTemplate.opsForValue().set(key, JSONObject.toJSONString(obj));
		}
	}

	public Long incRaw(String key) {
		return redisTemplate.opsForValue().increment(key);
	}

	public <T> T getObj(String key, Class<T> clazz) {
		String json = redisTemplate.opsForValue().get(key);
		if (!StringUtils.hasText(json)) {
			return null;
		}
		return JSONObject.parseObject(json, clazz);
	}

	public <T> List<T> getObjList(String key, Class<T> clazz) {
		String json = redisTemplate.opsForValue().get(key);
		if (!StringUtils.hasText(json)) {
			return null;
		}
		return JSONObject.parseArray(json, clazz);
	}

	public void putHashAll(String key, Map<String, String> map, Integer expireSec) {
		redisTemplate.opsForHash().putAll(key, map);
		redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getHashAll(String key) {
		if (!redisTemplate.hasKey(key)) {
			return null;
		}
		return (Map) redisTemplate.opsForHash().entries(key);
	}

	public <T> T getHashObj(String hashName, String key, Class<T> clazz) {
		String o = (String) redisTemplate.opsForHash().get(hashName, key);
		if (!StringUtils.hasText(o)) {
			return null;
		}
		return JSONObject.parseObject(o, clazz);
	}

	public String getHashRaw(String hashName, String key) {
		String o = (String) redisTemplate.opsForHash().get(hashName, key);
		if (!StringUtils.hasText(o)) {
			return null;
		}
		return o;
	}

	public <T> List<T> getHashArray(String hashName, String key, Class<T> clazz) {
		String o = (String) redisTemplate.opsForHash().get(hashName, key);
		if (!StringUtils.hasText(o)) {
			return null;
		}
		return JSONObject.parseArray(o, clazz);
	}

	public Long incHashRaw(String hashName, String key, long delta) {
		return redisTemplate.opsForHash().increment(hashName, key, delta);
	}

	public void putHashRaw(String hashName, String key, String str, Integer expireSec) {
		boolean hasKey = redisTemplate.hasKey(key);
		redisTemplate.opsForHash().put(hashName, key, str);
		if (!hasKey) {
			redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
		}
	}

	public void putHashRaw(String hashName, String key, String str) {
		redisTemplate.opsForHash().put(hashName, key, str);
	}

	public void putHashObj(String hashName, String key, Object obj, Integer expireSec) {
		boolean hasKey = redisTemplate.hasKey(key);
		redisTemplate.opsForHash().put(hashName, key, JSONObject.toJSONString(obj));
		if (!hasKey) {
			redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
		}
	}

	public void delHashObj(String hashName, String key) {
		redisTemplate.opsForHash().delete(hashName, key);
	}

	public void putRaw(String key, String value) {
		putRaw(key, value, null);
	}

	public void putRaw(String key, String value, Integer expireSec) {
		if (expireSec != null) {
			redisTemplate.opsForValue().set(key, value, expireSec, TimeUnit.SECONDS);
		} else {
			redisTemplate.opsForValue().set(key, value);
		}
	}

	public String getRaw(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void del(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	public void putSetRaw(String key, String member, Integer expireSec) {
		redisTemplate.opsForSet().add(key, member);
		redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
	}

	public void putSetRawAll(String key, String[] set, Integer expireSec) {
		redisTemplate.opsForSet().add(key, set);
		redisTemplate.expire(key, expireSec, TimeUnit.SECONDS);
	}

	public void removeSetRaw(String key, String member) {
		redisTemplate.opsForSet().remove(key, member);
	}

	public boolean isSetMember(String key, String member) {
		return redisTemplate.opsForSet().isMember(key, member);
	}

	/**
	 * 获取指定前缀的Key
	 * 
	 * @param prefix
	 * @return
	 */
	public Set<String> getPrefixKeySet(String prefix) {
		return redisTemplate.keys(prefix + "*");
	}

	public void delPrefixKey(String prefix) {
		Set<String> prefixKeySet = getPrefixKeySet(prefix);
		for (String key : prefixKeySet) {
			redisTemplate.delete(key);
		}
	}

}
