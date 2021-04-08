package ir.broker.maktab.broker.repository;

import ir.broker.maktab.broker.model.request.RequestSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestSubjectRepository extends JpaRepository<RequestSubject, Integer> {
    void deleteById(Integer id);
}
