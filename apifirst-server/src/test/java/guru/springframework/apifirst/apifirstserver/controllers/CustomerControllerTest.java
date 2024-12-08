package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.model.AddressDto;
import guru.springframework.apifirst.model.CustomerDto;
import guru.springframework.apifirst.model.NameDto;
import guru.springframework.apifirst.model.PaymentMethodDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt, Spring Framework Guru.
 */
@SpringBootTest
public class CustomerControllerTest extends BaseTest {

    @DisplayName("Test Create Customer")
    @Test
    void testCreateCustomer() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .name(NameDto.builder()
                        .lastName("Doe")
                        .firstName("John")
                        .build())
                .phone("555-555-5555")
                .email("john@example.com")
                .shipToAddress(AddressDto.builder()
                        .addressLine1("123 Main St")
                        .city("Denver")
                        .state("CO")
                        .zip("80216")
                        .build())
                .billToAddress(AddressDto.builder()
                        .addressLine1("123 Main St")
                        .city("Denver")
                        .state("CO")
                        .zip("80216")
                        .build())
                .build();

        mockMvc.perform(post(CustomerController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Transactional
    @DisplayName("Test Update Customer")
    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDto testCustomerDto = customerMapper.customerToDto(testCustomer);
        testCustomerDto.setPaymentMethods(List.of(PaymentMethodDto.builder()
                .displayName("My Card")
                .cardNumber(1341234121)
                .expiryMonth(12)
                .expiryYear(2025)
                .cvv(456).build()));

        mockMvc.perform(put(CustomerController.BASE_URL + "/{customerId}", testCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomerDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.paymentMethods[0].cvv", equalTo(456)));
    }

    @DisplayName("Get by Id")
    @Test
    void testGetCustomerById() throws Exception {
        mockMvc.perform(get(CustomerController.BASE_URL + "/{customerId}", testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCustomer.getId().toString()));
    }

    @DisplayName("Test List Customers")
    @Test
    void testListCustomers() throws Exception {
        mockMvc.perform(get(CustomerController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }
}
