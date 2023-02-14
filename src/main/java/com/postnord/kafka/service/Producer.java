package com.postnord.kafka.service;

public interface Producer {
    public void sendMessageSync(Object inputTopicName, Object errorTopicName, Object payload);
    public void sendMessageAsync(Object inputTopicName, Object errorTopicName, Object payload);
}
