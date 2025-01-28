package com.cleartax.training_superheroes.controllers;


import com.cleartax.training_superheroes.config.JedisConfig;
import com.cleartax.training_superheroes.config.SqsConfig;
import com.cleartax.training_superheroes.dto.Superhero;
import com.cleartax.training_superheroes.dto.SuperheroRequestBody;
import com.cleartax.training_superheroes.services.JedisHeroConsumer;
import com.cleartax.training_superheroes.services.SuperHeroConsumer;
import com.cleartax.training_superheroes.services.SuperheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SuperheroController {
    @Autowired
    private SuperheroService superheroService;
    @Autowired
    private SqsConfig sqsConfig;
    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SuperHeroConsumer superheroconsumer;

    private JedisHeroConsumer jedisheroconsumer;
    Jedis jedis= JedisConfig.getJedis();
    @Autowired
    public SuperheroController(SuperheroService superheroService,SqsClient sqsClient){
        this.superheroService = superheroService;
        this.sqsClient=sqsClient;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "username", defaultValue = "World") String username) {
        sqsClient.sendMessage(SendMessageRequest.builder().queueUrl(sqsConfig
                        .getQueueUrl())
                        .messageBody("maanik singla")
                        .build());
        ReceiveMessageResponse res=sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .build()
        );
        return String.format("Hello %s!,%s", username,sqsConfig.getQueueName());
    }



    @GetMapping("/superHeroConsumer")
    public String SuperHeroCo(){
        return superheroconsumer.consumeSuperhero();
    }


    @GetMapping("/consumeSuperheroJE")
    public String ConsumeSuperHeroJe(){
        return jedisheroconsumer.consumeSuperHero();
    }

    @GetMapping("/jedissuperhero")
    public void superHeroJe(String superhero){
        jedis.lpush(superhero,JedisConfig.GetQueueName());
        System.out.print("message");
    }
    @GetMapping("/update")
        public String SuperHero(){
        ReceiveMessageResponse receivedMessage = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .build());

        DeleteMessageResponse deletedMessage = sqsClient.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .receiptHandle(receivedMessage.messages().get(0).receiptHandle())
                .build());

        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .messageBody("updated")
                .build()
        );
        return receivedMessage.messages().get(0).body();
        }
    @GetMapping("/superhero")
    public Superhero getSuperhero(@RequestParam(value = "name", defaultValue = "Batman") String name,
                                  @RequestParam(value = "universe", defaultValue = "DC") String universe){
        return superheroService.getSuperhero(name, universe);
    }
    @PostMapping("/superhero")
    public Superhero persistSuperhero(@RequestBody SuperheroRequestBody superhero){
        return superheroService.persistSuperhero(superhero);
    }
    @PostMapping("/superheroes")
    public List<Superhero> persistSuperheroes(@RequestBody SuperheroRequestBody [] superhero){
        List<Superhero> superheroes= new ArrayList<>();
        for(SuperheroRequestBody b : superhero){
            Superhero newSuperhero=superheroService.persistSuperhero(b);
            superheroes.add(newSuperhero);
        }
     return superheroes;
    }
    @PutMapping("/update/{name}")
    public Superhero updateSuperhero(@PathVariable("name") String name,@RequestBody SuperheroRequestBody updatesuperhero){
          return superheroService.updateSuperhero(name,updatesuperhero);
    }
    @DeleteMapping("/delete/{name}")
    public Superhero deleteSuperhero(@PathVariable("name") String name){
         Superhero temp0=superheroService.deleteSuperhero(name);
         return temp0;
    }
}
