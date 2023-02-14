package com.junaid.opentel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.junaid.opentel.model.User;
import com.junaid.opentel.service.Producer;
import com.junaid.opentel.utils.Constants;

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

    @Value("otel.traces.api.version")
    private String tracesApiVersion;

    @Value("otel.metrics.api.version")
    private String metricsApiVersion;

    @Value("${spring.kafka.topic}")
    private String inputTopic;

    

    @Value("${spring.kafka.errortopic}")
    private String errorTopic;

  

    private final Tracer tracer =
        GlobalOpenTelemetry.getTracer("io.opentelemetry.traces.hello",
            tracesApiVersion);

    private final Meter meter =
        GlobalOpenTelemetry.meterBuilder("io.opentelemetry.metrics.hello")
            .setInstrumentationVersion(metricsApiVersion)
            .build();

    private LongCounter numberOfExecutions;

    private static final Logger logger = LoggerFactory.getLogger(Postnord.class);




    @PostConstruct
    public void createMetrics() {

        numberOfExecutions =
            meter
                .counterBuilder(Constants.NUMBER_OF_EXEC_NAME)
                .setDescription(Constants.NUMBER_OF_EXEC_DESCRIPTION)
                .setUnit("int")
                .build();

        meter
            .gaugeBuilder(Constants.HEAP_MEMORY_NAME)
            .setDescription(Constants.HEAP_MEMORY_DESCRIPTION)
            .setUnit("byte")
            .buildWithCallback(
                r -> {
                    r.record(getRuntime().totalMemory() - getRuntime().freeMemory());
                });

    }
    
    @PostMapping("/publishAsync")
    public void sendMessageAsync(@RequestBody User user){
        Span span = tracer.spanBuilder("producer").startSpan();
        try(Scope scope = span.makeCurrent()){
            producer.sendMessageAsync(inputTopic, errorTopic, user);
            numberOfExecutions.add(1);
        }finally {
            span.end();
        }
       
    }

    @PostMapping("/publishsync")
    public void sendMessageSync(@RequestBody User user){
        Span span = tracer.spanBuilder("producer").startSpan();
        try(Scope scope = span.makeCurrent()){
            producer.sendMessageSync(inputTopic, errorTopic, user);
            numberOfExecutions.add(1);
        }finally {
            span.end();
        }
       
    }
}
