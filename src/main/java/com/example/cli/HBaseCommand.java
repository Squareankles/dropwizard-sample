package com.example.cli;

import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

@Slf4j
public class HBaseCommand extends Command {

  private static final String CUSTOMER_TABLE = "customer";

  private static final String ADDRESS_FAMILY = "address";

  private static final String CONTACT_FAMILY = "contact";

  public HBaseCommand() {
    super("hbase", "build tables");
  }

  @Override
  public void configure(Subparser subparser) {

  }

  @Override
  public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
    log.info("Connecting to HBase");
    Configuration hBaseConfig = HBaseConfiguration.create();
    Connection connection = ConnectionFactory.createConnection(hBaseConfig);
    Admin admin = connection.getAdmin();
    try {

      if (!admin.tableExists(TableName.valueOf(CUSTOMER_TABLE))) {
        log.info("Creating customer table");
        ColumnFamilyDescriptor contactColumnFamily = ColumnFamilyDescriptorBuilder
            .newBuilder(Bytes.toBytes(CONTACT_FAMILY)).build();
        ColumnFamilyDescriptor addressColumnFamily = ColumnFamilyDescriptorBuilder
            .newBuilder(Bytes.toBytes(ADDRESS_FAMILY)).build();
        TableDescriptor tableDescriptor = TableDescriptorBuilder
            .newBuilder(TableName.valueOf(CUSTOMER_TABLE)).setColumnFamily(contactColumnFamily)
            .setColumnFamily(addressColumnFamily).build();

        admin.createTable(tableDescriptor);
        log.info("Customer table created");
      } else {
        log.info("Customer table already exists");
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      log.error(e.getCause().toString());
    } finally {
      log.info("Closing connection");
      connection.close();
    }

  }

}
