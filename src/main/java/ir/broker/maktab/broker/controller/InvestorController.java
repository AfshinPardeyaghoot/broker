package ir.broker.maktab.broker.controller;

import ir.broker.maktab.broker.config.PasswordConfig;
import ir.broker.maktab.broker.model.request.DBFile;
import ir.broker.maktab.broker.model.request.Request;
import ir.broker.maktab.broker.model.user.Role;
import ir.broker.maktab.broker.model.user.User;
import ir.broker.maktab.broker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@Controller
public class InvestorController {


    private RequestSubjectService requestSubjectService;
    private RequestService requestService;
    private DBFileService fileService;
    private RequestAnswerService answerService;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @Autowired
    public InvestorController(RequestSubjectService requestSubjectService, RequestService requestService, DBFileService fileService, RequestAnswerService answerService, PasswordEncoder passwordEncoder, UserService userService) {
        this.requestSubjectService = requestSubjectService;
        this.requestService = requestService;
        this.fileService = fileService;
        this.answerService = answerService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/investor/investorPanel")
    public String investorPanelPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        return "investor/investorPanel.html";
    }

    @RequestMapping("/investor/editInfo")
    public String editInfoPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "investor/editInvestorInfo";
    }

    @PostMapping("/investor/update")
    public String editInfo(@Valid User updatedUser, BindingResult bindingResult, @RequestParam("userId") Integer id) {
        String returnErrorUrl, returnValidUrl;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        returnErrorUrl = "redirect:/investor/editInfo";
        if (user.getRoles().get(0).equals(Role.ROLE_INVESTOR)) {
            returnValidUrl = "redirect:/investor/investorPanel";
        } else {
            returnValidUrl = "redirect:/manager/allUser";
        }
        if (bindingResult.hasErrors()) {
            return returnErrorUrl;
        } else {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setUsername(updatedUser.getUsername());
            user.setNationalId(updatedUser.getNationalId());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            userService.save(user);
            if (!user.getUsername().equals(updatedUser.getUsername())) {
                authentication.setAuthenticated(false);
                return "/login";
            }
            return returnValidUrl;
        }
    }

    @RequestMapping("/investor/newRequest")
    public String createRequestPage(Model model) {
        model.addAttribute("subjects", requestSubjectService.getAllRequestSubjects());
        return "investor/createTradeReguest.html";
    }

    @PostMapping("/investor/submitRequest")
    public String submitRequest(@RequestParam("files") MultipartFile[] files,
                                @RequestParam("subject") Integer subjectId,
                                @RequestParam("description") String description) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        requestService.save(files, subjectId, description, user);
        return "redirect:/investor/investorPanel";
    }

    @GetMapping("/investor/viewAllRequests")
    public String viewAllRequests(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<Request> requests = requestService.getRequestsByUser(user);
        model.addAttribute("requests", requests);
        return "investor/allRequests";
    }

    @GetMapping("/investor/requestWithDetail")
    public String requestWithDetail(@RequestParam("request-id") Integer requestId, Model model) {
        if (requestId == 0)
            return "redirect:/investor/viewAllRequests";
        Request request = requestService.getRequestById(requestId);
        Set<DBFile> files = request.getFileAttachments();
        model.addAttribute("request", request);
        model.addAttribute("files", files);
        System.out.println(files.size());
        return "investor/requestDetails";
    }

    @GetMapping("/investor/downloadAttachFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer fileId) {
        DBFile dbFile = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @GetMapping("/investor/viewAnswer")
    public String viewAnswer(@RequestParam("request-id") Integer requestId, Model model) {
        if (requestId == 0)
            return "redirect:/investor/viewAllRequests";

        if (!answerService.existsByRequest(requestService.getRequestById(requestId))) {
            return "redirect:/investor/viewAllRequests";
        }
        model.addAttribute("answer", answerService.getRequestAnswerByRequest(requestService.getRequestById(requestId)));
        return "/investor/showAnswer";
    }

    @GetMapping("/investor/editRequest")
    public String editRequestPage(@RequestParam("request-id") Integer requestId, Model model) {
        if (requestId == 0)
            return "redirect:/investor/viewAllRequests";

        if (answerService.existsByRequest(requestService.getRequestById(requestId))) {
            return "redirect:/investor/viewAllRequests";
        }
        Request request = requestService.getRequestById(requestId);
        DBFile dbFile = fileService.findByRequest(request);
        model.addAttribute("request", request);
        if (dbFile == null) {
            dbFile.setFileName(" ");
            dbFile.setId(0);
            model.addAttribute("file", dbFile);
        } else {
            model.addAttribute("file", dbFile);
        }
        return "/investor/editRequest";
    }

    @GetMapping("/investor/deleteFile/{id}/{requestId}")
    public String deleteFile(@PathVariable("id") Integer fileId, @PathVariable("requestId") Integer requestId, Model model) {
        System.out.println("file id :" + fileId);
        System.out.println("request id :" + requestId);
        requestService.getRequestById(requestId).getFileAttachments().remove(fileService.getFile(fileId));
        model.addAttribute("request-id", requestId);
        return "redirect:/investor/editRequest";
    }

    @GetMapping("investor/editPass")
    public String editPassPage() {
        return "investor/editPassword";
    }

    @PostMapping("investor/saveNewPass")
    public String editPassword(@RequestParam("old-pass") String oldPass,
                               @RequestParam("new-pass") String newPass,
                               @RequestParam("new-pass2") String newPass2) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        if (passwordEncoder.matches(oldPass, user.getPassword())
                && newPass.equals(newPass2)
                && PasswordConfig.isValidPassword(newPass)
        ) {
            userService.changeUserPassword(user, newPass);
            return "redirect:/investor/investorPanel";
        } else
            return "redirect:/investor/editPass";

    }


}
