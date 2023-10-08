package com.martingarrote.reservationmanagement.services;

import com.martingarrote.reservationmanagement.models.dtos.CustomerDTO;
import com.martingarrote.reservationmanagement.models.entities.Customer;
import com.martingarrote.reservationmanagement.models.entities.Reservation;
import com.martingarrote.reservationmanagement.repositories.CustomerRepository;
import com.martingarrote.reservationmanagement.repositories.ReservationRepository;
import com.martingarrote.reservationmanagement.utils.AuditUtils;
import com.martingarrote.reservationmanagement.utils.LocalDateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.*;
import static com.martingarrote.reservationmanagement.consts.ExceptionConsts.CustomerExceptions.AGE_SHOULD_BIGGER_THAN_EIGHTEEN;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    ReservationRepository reservationRepository;

    public List<CustomerDTO> listAll() {
        List<CustomerDTO> customersDTO = customerRepository.findAll().stream()
                .map(customer -> mapper.map(customer, CustomerDTO.class)).toList();

        return customersDTO;
    }

    public CustomerDTO findById(Long id) {
        Optional<Customer> optional = customerRepository.findById(id);
        CustomerDTO customerDTO = null;

        if (optional.isPresent()) {
            customerDTO = mapper.map(optional.get(), CustomerDTO.class);
        }

        return customerDTO;
    }

    public boolean deleteById(Long id) throws Exception {
        if (!customerRepository.existsById(id)) {
            return false;
        }

        if (!reservationRepository.findByCustomerId(id).isEmpty()) {
            throw new Exception(UNABLE_TO_DELETE);
        }

        customerRepository.deleteById(id);
        return true;
    }

    public Long create(CustomerDTO customerDTO) throws Exception {
        return save(customerDTO);
    }

    public Long update(CustomerDTO customerDTO, Long id) throws Exception {
        var optional = customerRepository.findById(id);
        AuditUtils auditUtils = new AuditUtils();

        if (optional.isPresent()) {
            Customer customer = optional.get();

            if (!customerDTO.getName().isEmpty()) {
                customer.setName(customerDTO.getName());
            }
            if (customerDTO.getDateOfBirth() != null) {
                customer.setDateOfBirth(customerDTO.getDateOfBirth());
            }
            if (!customerDTO.getCpf().isEmpty()) {
                customer.setCpf(customerDTO.getCpf());
            }
            if (!customerDTO.getEmail().isEmpty()) {
                customer.setEmail(customerDTO.getEmail());
            }

            auditUtils.AuditDefineFields(customer);

            customerRepository.save(customer);
            return customer.getId();

        } else {
            if (!customerRepository.existsById(id)) {
                return null;
            }
            throw new Exception(UPDATE_ERROR);
        }
    }

    public Long save(CustomerDTO customerDTO) throws Exception {

        AuditUtils auditUtils = new AuditUtils();

        if (LocalDateUtils.getAgeByDateOfBirth(customerDTO.getDateOfBirth()) < 18) {
            throw new Exception(AGE_SHOULD_BIGGER_THAN_EIGHTEEN);
        }

        try {
            Customer customer = mapper.map(customerDTO, Customer.class);

            auditUtils.AuditDefineFields(customer);

            Customer created = customerRepository.save(customer);

            return created.getId();
        } catch (Exception e) {
            throw new Exception(INSERT_ERROR);
        }
    }

}
