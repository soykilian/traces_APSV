package com.example.traceconsumer1.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.traceconsumer1.model.TransportationOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationOrderRepository extends CrudRepository<TransportationOrder,String> {
    //TransportationOrder findByTruckAndSt(String truck, int st);
}