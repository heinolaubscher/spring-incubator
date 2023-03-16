package entelect.training.incubator.spring.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.customer.model.Customer;
import entelect.training.incubator.spring.customer.repository.CustomerRepository;
import entelect.training.incubator.spring.customer.service.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.io.IOException;

@Component
public class PopulateCustomers implements CommandLineRunner {

    private CustomerRepository customerRepository;

    @Autowired
    public PopulateCustomers(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        CustomersService customersService = new CustomersService(customerRepository);

        InputStream inputStream = this.getClass().getResourceAsStream("/customers.json");

        // Use Jackson's ObjectMapper to deserialize the data into a List<Customer>
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Customer> customers = objectMapper.readValue(inputStream, new TypeReference<List<Customer>>(){});

            for (Customer customer: customers) {
                customersService.createCustomer(customer);
                System.out.println(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
