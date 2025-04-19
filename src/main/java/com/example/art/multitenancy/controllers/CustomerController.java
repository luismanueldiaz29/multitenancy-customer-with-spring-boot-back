package com.example.art.multitenancy.controllers;

import com.example.art.multitenancy.entities.Customer;
import com.example.art.multitenancy.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> getUsers() {
        return customerRepository.findAll();
    }

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
            return customerRepository.save(customer);
    }
}

