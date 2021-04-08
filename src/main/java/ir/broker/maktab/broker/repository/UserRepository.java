package ir.broker.maktab.broker.repository;

import ir.broker.maktab.broker.model.user.Role;
import ir.broker.maktab.broker.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findByNationalId(String nationalId);
    List<User> findUsersByFirstNameContaining(String firstName);
    List<User> findUsersByLastNameContaining(String lastName);
    List<User> findUsersByNationalIdContaining(String nationalId);
    List<User> findUsersByPhoneNumberContaining(String phoneNumber);
    List<User> findUsersByRolesNot(Role role);
}
