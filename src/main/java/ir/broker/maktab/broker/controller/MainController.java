package ir.broker.maktab.broker.controller;

import ir.broker.maktab.broker.config.PasswordConfig;
import ir.broker.maktab.broker.model.user.User;
import ir.broker.maktab.broker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;

import static ir.broker.maktab.broker.model.user.Role.*;

@Controller
public class MainController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public MainController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/sign-up")
    public String signUpPage(User user) {
        return "signUp";
    }

    @PostMapping("/sign-up")
    public String signUpProcess(@Valid User user, BindingResult bindingResult,
                                @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (bindingResult.hasErrors()){
            return "signUp";
        }
        else if(!PasswordConfig.isValidPassword(user.getPassword())){
            model.addAttribute("confirmPassError","رمز عبور باید شامل حروف بزرگ ، حروف کوچک ، اعداد و علامت ها باشد");
            return "signUp";
        }
        else if (!user.getPassword().equals(confirmPassword)){
            System.out.println("user.getPassword : "+user.getPassword());
            System.out.println("confirm Password : "+confirmPassword);
            model.addAttribute("confirmPassError","تکرار رمز عبور را بصورت صحیح وارد کنید");
            return "signUp";
        }
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

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }


}
