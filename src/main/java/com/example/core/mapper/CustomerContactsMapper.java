package com.example.core.mapper;

import com.example.db.entity.Customer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerContactsMapper implements Mapper<String, List<Customer>> {

  @SneakyThrows
  @Override
  public List<Customer> map(String from) {
    ObjectMapper mapper = new ObjectMapper();
    List<Customer> customers = mapper.readValue(from, new TypeReference<>() {
    });
    return customers;
  }
}
