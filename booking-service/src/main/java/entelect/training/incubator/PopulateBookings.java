package entelect.training.incubator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.model.Booking;
import entelect.training.incubator.repository.BookingRepository;
import entelect.training.incubator.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class PopulateBookings implements CommandLineRunner {

    private BookingRepository bookingRepository;

    @Autowired
    public PopulateBookings(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BookingService bookingService = new BookingService(bookingRepository);

        InputStream inputStream = this.getClass().getResourceAsStream("/bookings.json");

        // Use Jackson's ObjectMapper to deserialize the data into a List<Customer>
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Booking> bookings = objectMapper.readValue(inputStream, new TypeReference<List<Booking>>(){});

            for (Booking booking: bookings) {
                bookingService.createBooking(booking);
                System.out.println(booking);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
