package entelect.training.incubator.spring.customer;

import entelect.training.incubator.spring.customer.repository.CustomerRepository;
import entelect.training.incubator.spring.customer.service.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
