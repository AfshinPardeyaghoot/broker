package ir.broker.maktab.broker.repository;

import ir.broker.maktab.broker.model.request.Request;
import ir.broker.maktab.broker.model.request.RequestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestAnswerRepository extends JpaRepository<RequestAnswer, Integer> {
    Optional<RequestAnswer> getByRequest(Request request);
    boolean existsByRequest(Request request);
}
