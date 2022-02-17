package com.example.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.api.CustomerResponse;
import com.example.api.CustomerResponse.Customer;
import com.example.api.CustomerResponse.Customer.AddressFamily;
import com.example.api.CustomerResponse.Customer.ContactFamily;
import com.example.api.CustomerResponse.Customer.NameFamily;
import com.example.core.service.CustomerService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.util.Collections;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;

@ExtendWith(DropwizardExtensionsSupport.class)
class CustomerResourceTest {

  private static final CustomerService customerService = mock(CustomerService.class);

  private static final ResourceExtension EXT = ResourceExtension.builder()
      .addResource(new CustomerResource(customerService))
      .build();

  private static final String F_NAME = "fname";

  private static final String L_NAME = "lname";

  private static final String LINE_1 = "line1";

  private static final String LINE_2 = "line2";

  private static final String CITY = "city";

  private static final String LANDLINE = "08711551075";

  private static final String MOBILE = "0569785215";

  @Test
  void getTest() throws JSONException {
    // Arrange
    CustomerResponse responseObject = new CustomerResponse(
        Collections.singletonList(new Customer(new NameFamily(F_NAME, L_NAME),
            new AddressFamily(LINE_1, LINE_2, CITY), new ContactFamily(LANDLINE, MOBILE))));
    when(customerService.get("8943e2d6-6e37-4045-8e21-746440645e81")).thenReturn(responseObject);
    // Act
    final Response response = EXT.target("/v1/customers").request()
        .header("user-id", "8943e2d6-6e37-4045-8e21-746440645e81").get();

    // Assert
    assertNotNull(response);
    JSONAssert.assertEquals(
        "{\"customers\":[{\"name\":{\"firstName\":\"fname\",\"lastName\":\"lname\"},\"address\":{\"line1\":\"line1\",\"line2\":\"line2\",\"city\":\"city\"},\"contact\":{\"mobile\":\"0569785215\"}}]}",
        response.readEntity(String.class), true);
  }
}