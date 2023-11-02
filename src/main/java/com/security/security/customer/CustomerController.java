package com.security.security.customer;

import com.security.security.customer.models.Customer;
import com.security.security.customer.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    protected final CustomerRepository  customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return this.customerRepository.findAll();
    }

    record CreateCustomer(Integer age, String name, String email) {}

    @PostMapping("create")
    public Customer createCustomer(@RequestBody CreateCustomer customer) {
        System.out.println(customer);
        Customer ss = new Customer();
        ss.setAge(customer.age);
        ss.setName(customer.name);
        ss.setEmail(customer.email);
        System.out.println(ss);
        return this.customerRepository.save(ss);
    }

    @PutMapping("update/{customerId}")
    public Customer updateCustomer(@RequestBody Customer request, @PathVariable("customerId") Long customerId) {
        return this.customerRepository.findById(customerId).map(customer -> {
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setAge(request.getAge());
            return  this.customerRepository.save(customer);
        }).orElseGet(() -> {
            request.setId(customerId);
            return this.customerRepository.save(request);
        });
    }

    @DeleteMapping("delete/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") Long customerId) {
        this.customerRepository.deleteById(customerId);
        return "The has been deleted";
    }
}
