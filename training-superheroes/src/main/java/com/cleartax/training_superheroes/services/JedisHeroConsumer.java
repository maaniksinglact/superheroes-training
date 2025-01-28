package com.cleartax.training_superheroes.services;

import com.cleartax.training_superheroes.config.JedisConfig;
import redis.clients.jedis.Jedis;

public class JedisHeroConsumer {
    static Jedis jedis= JedisConfig.getJedis();
    public static String consumeSuperHero(){
        String message=jedis.rpop(JedisConfig.GetQueueName());
        if(message!=null){
            return message;
        }
        return "no";
    }
}
