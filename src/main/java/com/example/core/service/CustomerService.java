package com.example.core.service;

import com.example.api.CustomerResponse;
import com.google.inject.ImplementedBy;

@ImplementedBy(CustomerServiceImpl.class)
public interface CustomerService {

  CustomerResponse get(String userId);

}
