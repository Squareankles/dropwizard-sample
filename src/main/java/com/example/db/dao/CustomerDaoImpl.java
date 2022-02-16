package com.example.db.dao;

import com.example.db.entity.Customer;
import com.example.db.entity.Customer.AddressFamily;
import com.example.db.entity.Customer.ContactFamily;
import com.example.db.entity.Customer.NameFamily;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

@Slf4j
public class CustomerDaoImpl implements CustomerDao {

  private final Connection connection;

  @Inject
  public CustomerDaoImpl(Connection connection) {
    this.connection = connection;
  }

  @SneakyThrows
  @Override
  public void create(List<Customer> customers, String userId) {
    Table table = connection.getTable(TableName.valueOf("customer"));
    List<Put> puts = new ArrayList<>();

    for (Customer customer : customers) {
      String rowId = userId + "_" + UUID.randomUUID();
      Put p = new Put(Bytes.toBytes(rowId));
      p.addColumn(Bytes.toBytes("name"), Bytes.toBytes("first_name"),
          Bytes.toBytes(customer.getNameFamily().getFirstName()));
      p.addColumn(Bytes.toBytes("name"), Bytes.toBytes("last_name"),
          Bytes.toBytes(customer.getNameFamily().getLastName()));

      p.addColumn(Bytes.toBytes("address"), Bytes.toBytes("line_1"),
          Bytes.toBytes(customer.getAddressFamily().getLine1()));
      p.addColumn(Bytes.toBytes("address"), Bytes.toBytes("line_2"),
          Bytes.toBytes(customer.getAddressFamily().getLine2()));
      p.addColumn(Bytes.toBytes("address"), Bytes.toBytes("city"),
          Bytes.toBytes(customer.getAddressFamily().getCity()));

      p.addColumn(Bytes.toBytes("contact"), Bytes.toBytes("landLine"),
          Bytes.toBytes(customer.getContactFamily().getLandLine()));
      p.addColumn(Bytes.toBytes("contact"), Bytes.toBytes("mobile"),
          Bytes.toBytes(customer.getContactFamily().getMobile()));
      puts.add(p);
    }

    table.put(puts);
    table.close();

  }

  @SneakyThrows
  @Override
  public List<Customer> get(String userId) {
    Table table = connection.getTable(TableName.valueOf("customer"));
    // Getting the scan result
    Scan scan = new Scan();
    scan.setRowPrefixFilter(Bytes.toBytes(userId));
    ResultScanner scanner = table.getScanner(scan);
    List<Customer> customers = new ArrayList<>();
    // Reading values from scan result
    for (Result result = scanner.next(); result != null; result = scanner.next()) {
      String firstName = Bytes
          .toString(result.getValue(Bytes.toBytes("name"), Bytes.toBytes("first_name")));
      String lastName = Bytes
          .toString(result.getValue(Bytes.toBytes("name"), Bytes.toBytes("last_name")));

      String address1 = Bytes
          .toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("line_1")));
      String address2 = Bytes
          .toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("line_2")));
      String city = Bytes
          .toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("city")));

      String landline = Bytes
          .toString(result.getValue(Bytes.toBytes("contact"), Bytes.toBytes("landLine")));
      String mobile = Bytes
          .toString(result.getValue(Bytes.toBytes("contact"), Bytes.toBytes("mobile")));
      customers.add(new Customer(new NameFamily(firstName, lastName),
          new AddressFamily(address1, address2, city), new ContactFamily(landline, mobile)));
    }

    //closing the scanner
    scanner.close();

    table.close();

    return customers;
  }
}
