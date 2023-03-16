package entelect.training.incubator.service;

import entelect.training.incubator.model.Booking;
import entelect.training.incubator.model.BookingSearchRequestCustomer;
import entelect.training.incubator.model.BookingSearchRequestId;
import entelect.training.incubator.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookings() {
        Iterable<Booking> bookingIterable = bookingRepository.findAll();

        List<Booking> result = new ArrayList<>();
        bookingIterable.forEach(result::add);

        return result;
    }

    public List<Booking> searchCustomerID(BookingSearchRequestCustomer bookingSearchRequestCustomer) {
       List<Booking> bookings = bookingRepository.findByCustomerId(bookingSearchRequestCustomer.getCustomerId());

        return bookings;
    }
    public Booking getBooking(Integer id) {
        Optional<Booking> bookingOptional = bookingRepository.findByBookingId(id);
        return bookingOptional.orElse(null);
    }

}
