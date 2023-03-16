package entelect.training.incubator.spring.flight;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.flight.controller.FlightsController;
import entelect.training.incubator.spring.flight.model.Flight;
import entelect.training.incubator.spring.flight.repository.FlightRepository;
import entelect.training.incubator.spring.flight.service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FlightsPopulation implements CommandLineRunner {
    private FlightRepository flightRepository;

    @Autowired
    public FlightsPopulation(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        FlightsService flightsService = new FlightsService(flightRepository);

        InputStream inputStream = this.getClass().getResourceAsStream("/flights.json");

        // Use Jackson's ObjectMapper to deserialize the data into a List<Customer>
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Flight> flights = objectMapper.readValue(inputStream, new TypeReference<List<Flight>>(){});

            for (Flight flight: flights) {
                flightsService.createFlight(flight);
                System.out.println(flight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
