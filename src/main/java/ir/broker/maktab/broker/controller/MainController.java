package ir.broker.maktab.broker.controller;

import ir.broker.maktab.broker.model.user.User;
import ir.broker.maktab.broker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Arrays;

import static ir.broker.maktab.broker.model.user.Role.*;

@Controller
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/sign-up")
    public String signUpPage(User user) {
        return "signUp.html";
    }

    @PostMapping("/sign-up")
    public String signUpProcess(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "signUp.html";
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setIsEnable(true);
            user.setIsNonLocked(true);
            user.setRoles(Arrays.asList(ROLE_INVESTOR));
            userRepository.save(user);
            return "redirect:/investor/investorPanel";
        }
    }

    @RequestMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username).get();
        if (user.getRoles().contains(ROLE_MANAGER))
            return "redirect:/manager/managerPanel";
        else if (user.getRoles().contains(ROLE_ADMIN))
            return "redirect:/admin/adminPanel";
        else if (user.getRoles().contains(ROLE_INVESTOR))
            return "redirect:/investor/investorPanel";
        return null;
    }


}
