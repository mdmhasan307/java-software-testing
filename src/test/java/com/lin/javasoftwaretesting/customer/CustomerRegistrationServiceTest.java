package com.lin.javasoftwaretesting.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

     @Mock
     private CustomerRepository customerRepository;

     @Captor
     private ArgumentCaptor<Customer> customerArgumentCaptor;

    //Another way of using mockito
    //private CustomerRepository customerRepository= mock(CustomerRepository.class);

    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest= new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // given a phone number and a customer
        UUID id = UUID.randomUUID();
        String phoneNumber = "1232343456";
        Customer customer = new Customer(id, "md", phoneNumber);

        // ...a request
        CustomerRegistrationRequest request= new CustomerRegistrationRequest(customer);

        //... No customer with phone number passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // when
        underTest.registerNewCustomer(request);

        // then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue=customerArgumentCaptor.getValue();
        // assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(customer);
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);
    }

    @Test
    void itShouldNotSaveCustomersWhenCustomerExists() {
        // given a phone number and a customer
        UUID id = UUID.randomUUID();
        String phoneNumber = "1232343456";
        Customer customer = new Customer(id, "md", phoneNumber);

        // ...a request
        CustomerRegistrationRequest request= new CustomerRegistrationRequest(customer);

        //... an existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        // when
        underTest.registerNewCustomer(request);

        // then
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowWhenPhoneNumberIsTaken() {
        // given a phone number and a customer
        UUID id = UUID.randomUUID();
        String phoneNumber = "1232343456";
        Customer customer = new Customer(id, "md", phoneNumber);
        Customer customerTwo = new Customer(id, "hasan", phoneNumber);

        // ...a request
        CustomerRegistrationRequest request= new CustomerRegistrationRequest(customer);

        //... different customer with the particular phone number is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));

        // when

        // then
        assertThatThrownBy(()-> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is taken", phoneNumber));
        //finally
        then(customerRepository).should(never()).save(any(Customer.class));
    }
}