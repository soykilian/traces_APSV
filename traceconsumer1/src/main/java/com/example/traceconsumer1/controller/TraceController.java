package com.example.traceconsumer1.controller;

import com.example.traceconsumer1.model.Trace;
import com.example.traceconsumer1.repository.TraceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TraceController {
  private final TraceRepository tr;
  public TraceController(TraceRepository tr){
      this.tr=tr;
  }
  @GetMapping("/traces")
  List<Trace> all() {
      return(List<Trace>) tr.findAll();
  }
  @PostMapping("/traces")
    Trace newTraze(@RequestBody Trace newTraze){
      return tr.save(newTraze);
  }
  @GetMapping("/traces/{id}")
    Trace replaceTraze(@RequestBody Trace newTrace, @PathVariable String id){
      return tr.findById(id).map(Trace ->{
          Trace.setTraceId(newTrace.getTraceId());
          Trace.setTruck(newTrace.getTruck());
          Trace.setLastSeen(newTrace.getLastSeen());
          Trace.setLat(newTrace.getLat());
          Trace.setLng(newTrace.getLng());
          return tr.save(Trace);
      }).orElseGet(() -> {
          newTrace.setTraceId(id);
          return tr.save(newTrace);
      });
  }
  @DeleteMapping("/traces/{id}")
    void deleteTraze(@PathVariable String id){
      tr.deleteById(id);
  }
}
