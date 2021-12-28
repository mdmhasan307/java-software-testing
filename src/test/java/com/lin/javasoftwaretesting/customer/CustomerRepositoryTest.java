package com.lin.javasoftwaretesting.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "1232343456";
        String name="md";
        Customer customer = new Customer(id, name, phoneNumber);

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualToComparingFieldByField(customer);
                });
    }

    @Test
    void itShouldSaveCustomer() {
        // given
        UUID id=UUID.randomUUID();
        Customer customer= new Customer(id, "md", "1232343456");
        // when
        underTest.save(customer);
        // then
        Optional<Customer> optionalCustomer= underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c->{
//                    assertThat(c.getId()).isEqualTo(id);
//                    assertThat(c.getName()).isEqualTo("md");
//                    assertThat(c.getPhoneNumber()).isEqualTo("1232343456");
                    assertThat(c).isEqualToComparingFieldByField(customer);
                });

    }

//    @Test
//    void itShouldNotSaveCustomerWhenNameIsNull() {
//        // Given
//        UUID id = UUID.randomUUID();
//        String phoneNumber = "1232343456";
//        String name="md";
//        Customer customer = new Customer(id, name, phoneNumber);
//
//        // When
//        // Then
//        assertThatThrownBy(() -> underTest.save(customer))
//                .hasMessageContaining("not-null property references a null or transient value : com.lin.javasoftwaretesting.customer.Customer.name")
//                .isInstanceOf(DataIntegrityViolationException.class);
//
//    }
//
//    @Test
//    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
//        // Given
//        UUID id = UUID.randomUUID();
//        String phoneNumber = "1232343456";
//        String name="md";
//        Customer customer = new Customer(id, name, phoneNumber);
//
//        // When
//        // Then
//        assertThatThrownBy(() -> underTest.save(customer))
//                .hasMessageContaining("not-null property references a null or transient value : com.lin.javasoftwaretesting.customer.Customer.phoneNumber")
//                .isInstanceOf(DataIntegrityViolationException.class);
//
//    }

}