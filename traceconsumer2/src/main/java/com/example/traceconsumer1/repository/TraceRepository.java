package com.example.traceconsumer1.repository;
import com.example.traceconsumer1.model.Trace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRepository extends CrudRepository<Trace,String> {

}
