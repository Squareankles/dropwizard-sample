package com.example.db.dao;

import com.example.db.entity.Customer;
import com.google.inject.ImplementedBy;

@ImplementedBy(CustomerDaoImpl.class)
public interface CustomerDao {

  void create(Customer customer);

}
