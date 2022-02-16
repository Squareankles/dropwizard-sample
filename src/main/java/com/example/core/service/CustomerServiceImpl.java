package com.example.core.service;

import com.example.api.CustomerResponse;
import com.example.core.mapper.Mapper;
import com.example.db.dao.CustomerDao;
import com.example.db.entity.Customer;
import com.google.inject.Inject;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

  private final Mapper<List<Customer>, CustomerResponse> customerListMapper;

  private final CustomerDao customerDao;

  @Inject
  public CustomerServiceImpl(
      Mapper<List<Customer>, CustomerResponse> customerListMapper,
      CustomerDao customerDao) {
    this.customerListMapper = customerListMapper;
    this.customerDao = customerDao;
  }

  @Override
  public CustomerResponse get(String userId) {
    List<Customer> customer = this.customerDao.get(userId);
    CustomerResponse response = this.customerListMapper.map(customer);
    return response;
  }
}
