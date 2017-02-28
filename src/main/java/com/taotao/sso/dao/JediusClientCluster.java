package com.taotao.sso.dao;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Service
public class JediusClientCluster implements JedisClient {

    // @Autowired
    private JedisCluster jedisCluster;

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key, value);

    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public long hset(String hkey, String key, String value) {
        return jedisCluster.hset(hkey, key, value);
    }

    @Override
    public String hget(String hkey, String key) {
        return jedisCluster.hget(hkey, key);
    }

    @Override
    public long incr(String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public long expire(String key, long expireTimeInSecond) {
        return jedisCluster.expire(key, (int) expireTimeInSecond);
    }

    @Override
    public long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    @Override
    public long hdel(String hkey, String key) {
        return jedisCluster.hdel(hkey, key);
    }
    
    @Override
    public long del(String key) {
        return jedisCluster.del(key);
    }

}
