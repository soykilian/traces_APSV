package com.example.traceconsumer1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import com.example.traceconsumer1.model.Trace;
import com.example.traceconsumer1.model.TransportationOrder;
import com.example.traceconsumer1.repository.TransportationOrderRepository;


@SpringBootTest(classes = Traceconsumer1Application.class)
@Import(TestChannelBinderConfiguration.class)
class TraceConsumerAplicationTest {

    @Autowired
    private InputDestination input;
    @Autowired
    private TransportationOrderRepository repository;

    @BeforeEach
    void setUpOrders() {
        TransportationOrder o = new TransportationOrder();
        o.setToid("test-order");
        o.setTruck("test-truck");
        o.setOriginDate(100000000);
        o.setDstDate(o.getOriginDate() + (1000 * 60 * 12));
        o.setOriginLat(0.0);
        o.setOriginLong(0);
        o.setDstLat(44);
        o.setDstLong(88);
        o.setSt(0);
        repository.save(o);
    }

    @AfterEach
    void cleanUpOrders() {
        repository.deleteAll();
    }

    @Test
    void testOrderUpdate() {
        // 1. send the message to be processed asynchronously
        Trace t = new Trace("truck-1569233700000", "test-truck",
                1569233700000L, 38.42089633723801, -1.4491918734674392);
        Message<Trace> m = new GenericMessage<Trace>(t);
        input.send(m);
        try {
            // asynchronous processing
            Thread.sleep(1000);
            // 2. Check that the asynchronous function correctly updated the order
            TransportationOrder result = repository.findById("test-truck").
                    orElseThrow();
            assertEquals(result.getSt(), 0);
            assertEquals(result.getLastDate(), 1569233700000L);
            assertEquals(result.getLastLat(), 38.42089633723801);
            assertEquals(result.getLastLong(), -1.4491918734674392);
        } catch (NoSuchElementException e) {
            fail(); // the order should exist
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testOrderArrives() {
        // 1. send the message to be processed asynchronously
        // 2. Check that the asynchronous function correctly updated the order
    }

    @Test
    void testbadTrace() {

    }
}
