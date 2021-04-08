package ir.broker.maktab.broker.repository;

import ir.broker.maktab.broker.model.request.DBFile;
import ir.broker.maktab.broker.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DBFileRepository extends JpaRepository<DBFile, Integer> {
    Optional<DBFile> findDBFileById(Integer id);
    void deleteById(Integer id);
    Optional<DBFile> findByRequest(Request request);
}
