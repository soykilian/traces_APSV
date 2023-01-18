package com.example.traceconsumer1;

import com.example.traceconsumer1.model.Trace;
import com.example.traceconsumer1.model.TransportationOrder;
import com.example.traceconsumer1.repository.TraceRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.function.Consumer;

@SpringBootApplication
public class Traceconsumer1Application {
    public static final Logger log = LoggerFactory.getLogger(Traceconsumer1Application.class);
    @Autowired

    private  TraceRepository traceRepository;

    @Autowired
    private Environment env;
    public static void main(String[] args) {
        SpringApplication.run(Traceconsumer1Application.class, args);
    }
    @Bean("consumer")
    public Consumer<Trace> checkTrace() {
        return t-> {
            t.setTraceId(t.getTruck()+t.getLastSeen());
            traceRepository.save(t);
            String uri = env.getProperty("orders.server");
            RestTemplate restTemplate = new RestTemplate();
            TransportationOrder result = null;
            try {
                result = restTemplate.getForObject(uri + t.getTruck(), TransportationOrder.class);
            }catch(HttpClientErrorException.NotFound ex)  {
                result=null;
            }
            if (result != null && result.getSt() == 0){
                result.setLastDate(t.getLastSeen());
                result.setLastLat(t.getLat());
                result.setLastLong(t.getLng());
                if (result.distanceToDestination() < 10)
                    result.setSt(1);
                restTemplate.put(uri, result, TransportationOrder.class);
                log.info("Order update: " + result);
            }
        };
    }
}
