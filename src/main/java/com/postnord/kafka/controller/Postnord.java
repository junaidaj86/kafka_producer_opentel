package com.postnord.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.postnord.kafka.model.User;
import com.postnord.kafka.service.Producer;
import com.postnord.kafka.utils.Constants;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import static java.lang.Runtime.getRuntime;


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
