package com.example.db.dao;

import com.example.db.entity.Customer;
import com.google.inject.Inject;
import org.apache.hadoop.hbase.client.Connection;

public class CustomerDaoImpl implements CustomerDao {

  private final Connection connection;

  @Inject
  public CustomerDaoImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void create(Customer customer) {
    throw new UnsupportedOperationException();
  }
}
