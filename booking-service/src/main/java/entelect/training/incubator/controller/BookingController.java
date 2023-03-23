package entelect.training.incubator.controller;

import com.google.gson.JsonParser;
import entelect.training.incubator.client.gen.CaptureRewardsRequest;
import entelect.training.incubator.client.gen.CaptureRewardsResponse;
import entelect.training.incubator.client.gen.RewardsBalanceResponse;
import entelect.training.incubator.model.Booking;
import entelect.training.incubator.model.BookingSearchRequestCustomer;
import entelect.training.incubator.rewards.RewardsClient;
import entelect.training.incubator.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private RewardsClient rewardsClient;

    @Autowired
    public BookingController(BookingService bookingService, RewardsClient rewardsClient) {
        this.bookingService = bookingService;
        this.rewardsClient = rewardsClient;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        LOGGER.info("Processing customer creation request for customer={}", booking);

        WebClient webClient = WebClient.create();
        Mono<ResponseEntity<String>> responseCustomer = webClient.get()
                .uri("http://localhost:8201/customers/{id}", booking.getCustomerId())
                .headers(headers -> headers.setBasicAuth("admin", "is_a_lie"))
                .retrieve()
                .toEntity(String.class);

        Mono<ResponseEntity<String>> responseFlight = webClient.get()
                .uri("http://localhost:8202/flights/{id}", booking.getFlightId())
                .headers(headers -> headers.setBasicAuth("admin", "is_a_lie"))
                .retrieve()
                .toEntity(String.class);

        int customerId = -1;
        int flightId = -1;

        String customerJson = responseCustomer.block().getBody();
        JsonObject jsonObjectCustomer = new JsonParser().parse(customerJson).getAsJsonObject();
        customerId = jsonObjectCustomer.get("id").getAsInt();
        String flightJson = responseFlight.block().getBody();
        JsonObject jsonObjectFlight = new JsonParser().parse(flightJson).getAsJsonObject();
        flightId = jsonObjectFlight.get("id").getAsInt();
        String passportNumber = jsonObjectCustomer.get("passportNumber").getAsString();


        if(customerId < 0  || flightId < 0){
            return ResponseEntity.notFound().build();
        }

        final Booking savedBooking = bookingService.createBooking(booking);

        //TODO
        CaptureRewardsResponse captureRewardsResponse = rewardsClient.captureRewardsResponse(passportNumber, BigDecimal.valueOf(200));
        System.out.println(captureRewardsResponse.getBalance());



        LOGGER.trace("Booking created");
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getBookings() {
        LOGGER.info("Fetching all bookings");
        List<Booking> bookings = bookingService.getBookings();

        if (!bookings.isEmpty()) {
            LOGGER.trace("Found bookings");
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }

        LOGGER.info("No booking could be found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBookingsReference(@RequestBody BookingSearchRequestCustomer searchRequest) {
        LOGGER.info("Processing bookings search request for request {}", searchRequest);
        List<Booking> bookings = bookingService.searchCustomerID(searchRequest);
        if (bookings != null) {
            return ResponseEntity.ok(bookings);
        }
        LOGGER.trace("Bookings not found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        LOGGER.info("Processing customer search request for customer id={}", id);
        Booking booking = this.bookingService.getBooking(id);
        if (booking != null) {
            LOGGER.trace("Found customer");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }
        LOGGER.trace("Customer not found");
        return ResponseEntity.notFound().build();
    }
}