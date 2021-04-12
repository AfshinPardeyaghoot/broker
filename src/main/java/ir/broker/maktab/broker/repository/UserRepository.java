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
    List<User> findUsersByFirstNameContainingAndRolesNot(String firstName, Role role);
    List<User> findUsersByLastNameContainingAndRolesNot(String lastName, Role role);
    List<User> findUsersByNationalIdContainingAndRolesNot(String nationalId, Role role);
    List<User> findUsersByPhoneNumberContainingAndRolesNot(String phoneNumber, Role role);
    List<User> findUsersByRolesNot(Role role);
}
