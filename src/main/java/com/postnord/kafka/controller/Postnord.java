package com.postnord.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postnord.kafka.model.User;
import com.postnord.kafka.service.Producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@RestController
public class Postnord {

    @Autowired
    Producer producer;

  

    @Value("${spring.kafka.topic}")
    private String inputTopic;

    

    @Value("${spring.kafka.errortopic}")
    private String errorTopic;

    private static final Logger logger = LoggerFactory.getLogger(Postnord.class);





    
    @PostMapping("/publishAsync")
    public void sendMessageAsync(@RequestBody User user){
        logger.debug("hellooooooo", errorTopic, user);
            producer.sendMessageAsync(inputTopic, errorTopic, user);
       
       
    }

    @PostMapping("/publishsync")
    public void sendMessageSync(@RequestBody User user){
            producer.sendMessageSync(inputTopic, errorTopic, user);
        
       
    }
}
