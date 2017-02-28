package com.taotao.sso.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JediusClientSingle implements JedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String set(String key, String value) {
        Jedis client = jedisPool.getResource();
        String result = client.set(key, value);
        client.close();
        return result;
    }

    @Override
    public String get(String key) {
        Jedis client = jedisPool.getResource();
        String result = client.get(key);
        client.close();
        return result;
    }

    @Override
    public long hset(String hkey, String key, String value) {
        Jedis client = jedisPool.getResource();
        Long result = client.hset(hkey, key, value);
        client.close();
        return result;
    }

    @Override
    public String hget(String hkey, String key) {
        Jedis client = jedisPool.getResource();
        String result = client.hget(hkey, key);
        client.close();
        return result;
    }

    @Override
    public long incr(String key) {
        Jedis client = jedisPool.getResource();
        Long incr = client.incr(key);
        client.close();
        return incr;
    }

    @Override
    public long expire(String key, long expireTimeInSecond) {
        Jedis client = jedisPool.getResource();
        Long expire = client.expire(key, (int) expireTimeInSecond);
        client.close();
        return expire;
    }

    @Override
    public long ttl(String key) {
        Jedis client = jedisPool.getResource();
        Long ttl = client.ttl(key);
        client.close();
        return ttl;
    }

    @Override
    public long hdel(String hkey, String key) {
        Jedis client = jedisPool.getResource();
        Long delete = client.hdel(hkey, key);
        client.close();
        return delete;
    }

    @Override
    public long del(String key) {
        Jedis client = jedisPool.getResource();
        Long delete = client.del(key);
        client.close();
        return delete;
    }
    

}
