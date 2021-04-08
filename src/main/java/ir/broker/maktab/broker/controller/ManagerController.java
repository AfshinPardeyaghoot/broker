package ir.broker.maktab.broker.controller;

import ir.broker.maktab.broker.config.PasswordConfig;
import ir.broker.maktab.broker.model.request.DBFile;
import ir.broker.maktab.broker.model.request.Request;
import ir.broker.maktab.broker.model.request.RequestStatus;
import ir.broker.maktab.broker.model.request.RequestSubject;
import ir.broker.maktab.broker.model.user.User;
import ir.broker.maktab.broker.repository.UserRepository;
import ir.broker.maktab.broker.service.RequestAnswerService;
import ir.broker.maktab.broker.service.RequestService;
import ir.broker.maktab.broker.service.RequestSubjectService;
import ir.broker.maktab.broker.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.security.auth.Subject;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static ir.broker.maktab.broker.model.user.Role.ROLE_ADMIN;
import static ir.broker.maktab.broker.model.user.Role.ROLE_INVESTOR;

@Controller
public class ManagerController {

    private UserService userService ;
    private  PasswordEncoder passwordEncoder;
    private RequestService requestService ;
    private RequestSubjectService subjectService ;
    private RequestAnswerService answerService ;

    @Autowired
    public ManagerController(UserService userService, PasswordEncoder passwordEncoder, RequestService requestService, RequestSubjectService subjectService, RequestAnswerService answerService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.requestService = requestService;
        this.subjectService = subjectService;
        this.answerService = answerService;
    }

    @RequestMapping("manager/managerPanel")
    public String managerPanelPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        return "manager/managerPanel";
    }

    @GetMapping("/manager/createNewUser")
    public String crateNewUserPage(User user) {
        return "manager/createNewUser";
    }

    @PostMapping("manager/saveUser")
    public String saveNewUser(@Valid User user, @RequestParam("role") String role, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "manager/createNewUser";
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setIsEnable(true);
            user.setIsNonLocked(true);
            if (role.equals("admin")) {
                user.setRoles(Arrays.asList(ROLE_ADMIN));
            } else {
                user.setRoles(Arrays.asList(ROLE_INVESTOR));
            }
            userService.save(user);
            return "redirect:/manager/managerPanel";
        }
    }

    @RequestMapping("/manager/allUser")
    public String showAllUser(Model model){
        model.addAttribute("users",userService.usersNotManager());
        return "manager/showAllUsers";
    }

    @GetMapping("/manager/editUser")
    public String editUserPage(@RequestParam("userNationalId") String nationalId ,  Model model){
        if(nationalId == "")
            return "redirect:/manager/allUser";
        model.addAttribute("user", userService.findUserByNationalId(nationalId));
        return "investor/editInvestorInfo";
    }

    @GetMapping("/manager/showUserRequests")
    public String showUserRequests(@RequestParam("userNationalId") String nationalId, Model model){
        if(nationalId == "")
            return "redirect:/manager/allUser";
        model.addAttribute("requests",requestService.getRequestsByUser(userService.findUserByNationalId(nationalId)));
        return "investor/allRequests";
    }

    @GetMapping("/manager/searchUsers")
    public String limitUsersByFields(@RequestParam("searchField") String searchField,@RequestParam("value")String value, Model model){
        List<User> users ;
        if (searchField.equals("firstName"))
            users = userService.findUsersLikeFirstName(value);
        else if(searchField.equals("lastName"))
            users = userService.findUsersLikeLastName(value);
        else if(searchField.equals("nationalId"))
            users = userService.findUsersLikeNationalId(value);
        else if(searchField.equals("phoneNumber"))
            users = userService.findUsersLikePhoneNumber(value);
        else
            users = userService.getAllUsers();
        model.addAttribute("users",users);
        return "manager/showAllUsers";
    }

    @GetMapping("/manager/allRequests")
    public String showAllRequests(Model model){
        model.addAttribute("requests",requestService.getAllRequests());
        return "manager/showAllRequests";
    }

    @GetMapping("/manager/requestWithDetails")
    public String showRequestWithDetails(@RequestParam("request-id")Integer requestId, Model model){
        if (requestId == 0)
            return "redirect:/manager/allRequests";
        Request request = requestService.getRequestById(requestId);
        Set<DBFile> files = request.getFileAttachments();
        model.addAttribute("request", request);
        model.addAttribute("files", files);
        System.out.println(files.size());
        return "investor/requestDetails";
    }

    @GetMapping("/manager/limitRequestsList")
    public String limitRequestsList(@RequestParam("requestStatus") String requestStatus,
                                    @RequestParam("date") String data, Model model){
        List<Request> requests = null;
        if (requestStatus != "" && data != ""){
            try {
                requests = requestService.getByDateAndRequestStatus(new SimpleDateFormat("yyyy-MM-dd").parse(data), RequestStatus.valueOf(requestStatus));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(requestStatus != ""){
            requests = requestService.getByRequestStatus(RequestStatus.valueOf(requestStatus));
        }else if(data != ""){
            try {
                requests = requestService.getByDate(new SimpleDateFormat("yyyy-MM-dd").parse(data));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("requests",requests);
        return "manager/showAllRequests";
    }

    @GetMapping("/manager/viewAnswer")
    public String viewAnswer(@RequestParam("request-id")Integer requestId, Model model){
        if (requestId == 0)
            return "redirect:/manager/allRequests";

        if (!answerService.existsByRequest(requestService.getRequestById(requestId))){
            return "redirect:/manager/allRequests";
        }

        model.addAttribute("answer",answerService.getRequestAnswerByRequest(requestService.getRequestById(requestId)));
        return "/investor/showAnswer";
    }

    @GetMapping("/manager/requestSubjects")
    public String requestSubjects(Model model){
        model.addAttribute("subjects",subjectService.getAllRequestSubjects());
        return "manager/requestSubject";
    }

    @GetMapping("/manager/deleteSubject")
    public String deleteSubject(@RequestParam("subjectId")Integer subjectId){
        if (subjectId == 0)
            return "redirect:/manager/requestSubjects";
        else {
            subjectService.deleteById(subjectId);
            return "redirect:/manager/requestSubjects";
        }
    }

    @PostMapping("/manager/newSubject")
    public String newSubject(@RequestParam("subject")String subject){
        if (subject == "")
            return "redirect:/manager/requestSubjects";
        else {
            RequestSubject subject1 = new RequestSubject();
            subject1.setSubject(subject);
            subjectService.save(subject1);
            return "redirect:/manager/requestSubjects";
        }
    }

    @GetMapping("/manager/editUserPass")
    public String editUserPassPage(@RequestParam("userNationalId")String userId, Model model){
        System.out.println(userId);
        model.addAttribute("id",userId);
        return "manager/editUserPass";
    }

    @PostMapping("/admin/userNewPass")
    public String saveUserNewPass(@RequestParam("new-pass")String newPass, @RequestParam("user-id")String userNationalId){
        User user = userService.findUserByNationalId(userNationalId);
        if (PasswordConfig.isValidPassword(newPass)){
            userService.changeUserPassword(user,newPass);
            return "redirect:/manager/allUser";
        }else return "/manager/editUserPass";
    }

    @PostMapping("/manager/ChangeUserLockSatus")
    public String changeUserLockStatus(@RequestParam("userNationalId") String nationalId){
        User user = userService.findUserByNationalId(nationalId);
        if (user.getIsNonLocked().equals(true))
            user.setIsNonLocked(false);
        else
            user.setIsNonLocked(true);
        userService.save(user);
        return "redirect:/manager/allUser" ;
    }

}
