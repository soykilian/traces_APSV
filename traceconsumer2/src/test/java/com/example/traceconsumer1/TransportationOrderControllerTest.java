package com.example.traceconsumer1;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;


import java.io.BufferedReader;

import java.io.FileReader;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;


import com.example.traceconsumer1.repository.TraceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.core.io.ClassPathResource;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.RequestBuilder;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import com.example.traceconsumer1.repository.TransportationOrderRepository;

import com.example.traceconsumer1.controller.TransportationOrderController;

import com.example.traceconsumer1.model.TransportationOrder;



@WebMvcTest(TransportationOrderController.class)
public class TransportationOrderControllerTest {


    @InjectMocks
    private TransportationOrderController business;


    @MockBean
    private TransportationOrderRepository repository;

    @MockBean
    private TraceRepository traceRepository;


    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetOrders() throws Exception {

        when(repository.findAll()).thenReturn(getAllTestOrders());
        RequestBuilder request = MockMvcRequestBuilders
                .get("/orders")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(20)))
                .andReturn();
    }
    @Test
    public void testGetOrder() throws Exception {
        String id = "8962ZKR";
        when(repository.findById(id)).thenReturn(Optional.of(
                new TransportationOrder("28", id ,1591682400000L,
                        40.4562191,-3.8707211,1591692196000L,42.0206372,-4.5330132,
                        0,0.0,0.0,0)));
       RequestBuilder request = MockMvcRequestBuilders
               .get("/orders/" +id)
               .accept(MediaType.APPLICATION_JSON);
       MvcResult res = mockMvc.perform(request)
               .andExpect((status().isOk()))
               .andExpect(jsonPath("$.truck").value(id))
               .andReturn();
    }

    @Test
    public void testWrongOrder() throws Exception {
        String id = "8962ZKG";
        when(repository.findById(id)).thenReturn(Optional.empty());
        RequestBuilder request = MockMvcRequestBuilders
                .get("/orders/" +id)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult res = mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private List<TransportationOrder> getAllTestOrders(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<TransportationOrder> orders =
                new ArrayList<TransportationOrder>();
        TransportationOrder order = null;
        try(BufferedReader br = new BufferedReader(new FileReader(
                new ClassPathResource("orders.json").getFile()))) {
            for(String line; (line = br.readLine()) != null; ) {
                order = objectMapper.readValue(line, TransportationOrder.class);
                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }
}

