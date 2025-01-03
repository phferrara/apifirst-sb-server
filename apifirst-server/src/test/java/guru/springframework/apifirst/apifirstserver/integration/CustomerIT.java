package guru.springframework.apifirst.apifirstserver.integration;

import guru.springframework.apifirst.ApiClient;
import guru.springframework.apifirst.client.CustomerApi;
import guru.springframework.apifirst.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerIT {
    CustomerApi customerApi;

    @BeforeEach
    void setup() {
        ApiClient apiClient = new ApiClient(new RestTemplate());
        apiClient.setBasePath("http://localhost:8080/");
        customerApi = new CustomerApi(apiClient);
    }

    @Test
    void testCustomer() {
        List<CustomerDto> customers = customerApi.listCustomers();
        assertThat(customers.size()).isGreaterThan(0);
    }
}
