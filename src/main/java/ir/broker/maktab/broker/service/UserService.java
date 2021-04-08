package ir.broker.maktab.broker.service;

import ir.broker.maktab.broker.model.user.Role;
import ir.broker.maktab.broker.model.user.User;
import ir.broker.maktab.broker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(s);
        if (user.isPresent())
            return user.get();
        else
            throw new UsernameNotFoundException(" Username not found ");
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).get();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByNationalId(String nationalId) {
        return userRepository.findByNationalId(nationalId).get();
    }

    public List<User> findUsersLikeFirstName(String firstName) {
        return userRepository.findUsersByFirstNameContaining(firstName);
    }

    public List<User> findUsersLikeLastName(String lastName) {
        return userRepository.findUsersByLastNameContaining(lastName);
    }

    public List<User> findUsersLikeNationalId(String nationalId) {
        return userRepository.findUsersByNationalIdContaining(nationalId);
    }

    public List<User> findUsersLikePhoneNumber(String phoneNumber) {
        return userRepository.findUsersByPhoneNumberContaining(phoneNumber);
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public List<User> usersNotManager(){
        return userRepository.findUsersByRolesNot(Role.ROLE_MANAGER);
    }
}
