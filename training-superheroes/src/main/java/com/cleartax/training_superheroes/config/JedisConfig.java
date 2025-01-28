package com.cleartax.training_superheroes.config;

import redis.clients.jedis.Jedis;

public class JedisConfig {
    private static final String REDIS_HOST = "localhost";  // Default Redis host
    private static final int REDIS_PORT = 6379;            // Default Redis port
    private static final String Queue_Name="superhero";
    // Create and return a Jedis connection
    public static Jedis getJedis() {
        return new Jedis(REDIS_HOST, REDIS_PORT);
    }
    public static String GetQueueName(){
        return Queue_Name;
    }
}
