package ir.broker.maktab.broker.repository;

import ir.broker.maktab.broker.model.request.Request;
import ir.broker.maktab.broker.model.request.RequestStatus;
import ir.broker.maktab.broker.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> getRequestsByUser(User user);
    Optional<Request> getRequestsById(Integer id);
    List<Request> getRequestsByDateAndRequestStatus(Date date, RequestStatus requestStatus);
    List<Request> getRequestsByDate(Date date);
    List<Request> getRequestsByRequestStatus(RequestStatus requestStatus);
}
