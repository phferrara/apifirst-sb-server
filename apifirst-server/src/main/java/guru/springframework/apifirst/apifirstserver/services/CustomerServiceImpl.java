package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.domain.Customer;
import guru.springframework.apifirst.apifirstserver.mappers.CustomerMapper;
import guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import guru.springframework.apifirst.model.CustomerDto;
import guru.springframework.apifirst.model.CustomerPatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * Created by jt, Spring Framework Guru.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    @Override
    public CustomerDto updateCustomer(UUID customerId, CustomerDto customer) {
        Customer existingCustomer = customerRepository.findById(customerId).orElseThrow();
        customerMapper.updateCustomer(customer, existingCustomer);

        return customerMapper.customerToDto(customerRepository.save(existingCustomer));
    }

    @Override
    public CustomerDto patchCustomer(UUID customerId, CustomerPatchDto customer) {
        Customer existingCustomer = customerRepository.findById(customerId).orElseThrow();
        customerMapper.patchCustomer(customer, existingCustomer);

        return customerMapper.customerToDto(customerRepository.save(existingCustomer));
    }


    @Transactional
    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        Customer savedCustomer = customerRepository.save(customerMapper.dtoToCustomer(customer));
        customerRepository.flush();
        return customerMapper.customerToDto(savedCustomer);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .map(customerMapper::customerToDto)
                .toList();
    }

    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return customerMapper.customerToDto(customerRepository.findById(customerId).orElseThrow());
    }
}
