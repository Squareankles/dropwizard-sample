package com.example.core.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.db.entity.Customer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerContactsMapperTest {

  Mapper<String, List<Customer>> mapper = new CustomerContactsMapper();

  @Test
  void mapTest() throws IOException {
    // Arrange
    String json = IOUtils.toString(
        this.getClass().getResourceAsStream("/customer1.json"),
        StandardCharsets.UTF_8
    );

    // Act
    List<Customer> customer = mapper.map(json);

    // Assert
    assertNotNull(customer);
    assertEquals(4, customer.size());
  }
}