package entelect.training.incubator.repository;

import entelect.training.incubator.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {

    List<Booking> findByCustomerId(int id);

    Optional<Booking> findByBookingId(int bookingId);
}
