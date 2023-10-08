package com.martingarrote.reservationmanagement.services;

import com.martingarrote.reservationmanagement.models.dtos.CustomerDTO;
import com.martingarrote.reservationmanagement.models.entities.Customer;
import com.martingarrote.reservationmanagement.repositories.CustomerRepository;
import jakarta.validation.constraints.Null;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.INSERT_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository repository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    CustomerService service;

    @Test
    public void save_ShouldSave() throws Exception {
        // Arrange
        Customer customer = createCustomerEntity();
        Customer createdCustomer = createCustomerEntity();
        CustomerDTO customerDTO = createCustomerDTO();
        when(mapper.map(customerDTO, Customer.class)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(createdCustomer);

        // Act
        Long id = service.save(customerDTO);

        // Asserts
        verify(mapper).map(customerDTO, Customer.class);
        verify(repository, times(1)).save(customer);
        verifyNoMoreInteractions(repository);
        assertThat(id).isEqualTo(1L);
    }

    @Test()
    public void save_ShouldNotSave() throws Exception {
        CustomerDTO customerDTO = createCustomerDTO();

        Throwable exception = catchThrowable(() -> service.save(customerDTO));

        System.out.println(exception.toString());
        assertThat(exception.getMessage()).isEqualTo(INSERT_ERROR);
    }

    @Test(expected = Exception.class)
    public void save_ShouldNotSaveBecauseAgeException() throws Exception {
        CustomerDTO customerDTO = createCustomerDTO();
        customerDTO.setDateOfBirth(LocalDate.now());

        service.save(customerDTO);

    }

    @Test
    public void create_ShouldCreate() throws Exception {
        // Arrange
        Customer customer = createCustomerEntity();
        Customer createdCustomer = createCustomerEntity();
        CustomerDTO customerDTO = createCustomerDTO();
        when(mapper.map(customerDTO, Customer.class)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(createdCustomer);

        // Act
        Long id = service.create(customerDTO);

        // Asserts
        assertThat(id).isEqualTo(1L);

    }

    @Test(expected = Exception.class)
    public void create_ShouldNotCreate() throws Exception {
        CustomerDTO customerDTO = createCustomerDTO();

        when(service.save(customerDTO)).thenThrow(new Exception());
    }

    @Test
    public void listAll_ShouldList() {
        List<CustomerDTO> customersDTO = repository.findAll().stream()
                .map(customer -> mapper.map(customer, CustomerDTO.class)).toList();
        when(repository.findAll().stream()
                .map(customer -> mapper.map(customer, CustomerDTO.class)).toList()).thenReturn(customersDTO);

        List<CustomerDTO> expectedCustomersDTO = service.listAll();

        assertArrayEquals(expectedCustomersDTO.toArray(), customersDTO.toArray());
    }

    @Test
    public void listAll_ShouldNotListAnyCustomer() {
        when(repository.findAll()).thenReturn(null);

        List<CustomerDTO> returnedCustomersDTO = service.listAll();

        assertThat(returnedCustomersDTO).isNull();
    }


    @Test
    public void findById_ShouldGet() {
        Long id = 1L;
        Customer customer = createCustomerEntity();
        CustomerDTO returnedCustomerDTO = mapper.map(customer, CustomerDTO.class);

        when(repository.findById(id)).thenReturn(Optional.of(customer));
        when(mapper.map(customer, CustomerDTO.class)).thenReturn(returnedCustomerDTO);


        CustomerDTO expectedCustomerDTO = service.findById(id);

        assertThat(expectedCustomerDTO).isEqualTo(returnedCustomerDTO);
    }

    @Test
    public void findById_ShouldNotGetAnyCustomer() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        CustomerDTO returnedCustomerDTO = service.findById(id);

        assertThat(returnedCustomerDTO).isNull();
    }

    public Customer createCustomerEntity() {
        LocalDate dateOfBirth = LocalDate.of(1990, 10, 5);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return new Customer(1L, "Jose", dateOfBirth, "12345678910", "email@gmail.com");
    }

    public CustomerDTO createCustomerDTO() {
        LocalDate dateOfBirth = LocalDate.of(1990, 10, 5);

        return new CustomerDTO(1L, "Jose", dateOfBirth, "12345678910", "email@gmail.com");
    }

}
