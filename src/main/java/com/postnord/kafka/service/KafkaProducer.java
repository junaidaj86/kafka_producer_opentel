package com.postnord.kafka.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class KafkaProducer implements Producer {

    
  Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

  @Autowired
  KafkaTemplate<Object, Object> kafkaTemplate;


  @Override
  public void sendMessageSync(Object inputTopicName, Object errorTopicName, Object payload) {
    logger.info("KafkaProducer:sendMessage:producing message to topic "+ inputTopicName);
      try {
          kafkaTemplate.send((String)inputTopicName, payload).get();
          logger.info("KafkaProducer:sendMessageAsync:producing message to topic "+ inputTopicName + " was successful");
      } catch (InterruptedException | ExecutionException e) {
        logger.error("KafkaProducer:sendMessage:error in publishing payload to topic  "+ inputTopicName);
      }
      
  }

  @Override
  @Async
  public void sendMessageAsync(Object inputTopicName, Object errorTopicName, Object payload) {
      CompletableFuture<SendResult<Object, Object>> future = kafkaTemplate.send((String)inputTopicName, payload);
      future.whenComplete((msg, ex) -> {
          if (ex != null) {
            logger.error("KafkaProducer:sendMessageAsync:error in publishing payload to topic  "+ inputTopicName);
          } else {
            logger.info("KafkaProducer:sendMessageAsync:producing message to topic "+ inputTopicName + " was successful");
          }
         
        });
      
      
  }

}
