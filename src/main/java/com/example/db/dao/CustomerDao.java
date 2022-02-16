package com.example.db.dao;

import com.example.db.entity.Customer;
import com.google.inject.ImplementedBy;
import java.util.List;

@ImplementedBy(CustomerDaoImpl.class)
public interface CustomerDao {

  void create(List<Customer> customers, String userId);

  List<Customer> get(String userId);

}
