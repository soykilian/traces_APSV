package com.example.traceconsumer1.repository;
import com.example.traceconsumer1.model.Trace;
import org.springframework.data.repository.CrudRepository;

public interface TraceRepository extends CrudRepository<Trace,String> {

}
