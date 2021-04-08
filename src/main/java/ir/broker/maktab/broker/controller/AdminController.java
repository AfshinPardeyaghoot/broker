package ir.broker.maktab.broker.controller;

import ir.broker.maktab.broker.model.request.*;
import ir.broker.maktab.broker.service.DBFileService;
import ir.broker.maktab.broker.service.RequestAnswerService;
import ir.broker.maktab.broker.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Set;


@Controller
public class AdminController {

    private RequestService requestService;
    private DBFileService fileService;
    private RequestAnswerService requestAnswerService;


    @Autowired
    public AdminController(RequestService requestService, DBFileService fileService, RequestAnswerService requestAnswerService) {
        this.requestService = requestService;
        this.fileService = fileService;
        this.requestAnswerService = requestAnswerService;
    }

    @RequestMapping("admin/adminPanel")
    public String managerPanelPage(Model model) {
        model.addAttribute("requests", requestService.getByRequestStatus(RequestStatus.ATTENDING));
        return "admin/adminPanel";
    }

    @GetMapping("/admin/requestDetails")
    public String requestDetails(@RequestParam("request-id") Integer requestId, Model model) {
        Request request = requestService.getRequestById(requestId);
        Set<DBFile> files = request.getFileAttachments();
        model.addAttribute("request", request);
        model.addAttribute("files", files);
        System.out.println(files.size());
        return "admin/requestDetailsAdmin";
    }

    @GetMapping("/admin/downloadAttachFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer fileId) {
        DBFile dbFile = fileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @GetMapping("/admin/answerPage")
    public String answerPage(@RequestParam("request-id") Integer requestId, Model model) {
        model.addAttribute("requestId", requestId);
        return "admin/requestAnswer";
    }

    @GetMapping("/admin/submitAnswer")
    public String submitAnswer(@RequestParam("status") String status,
                               @RequestParam("description") String description,
                               @RequestParam("requestId") Integer requestId) {
        AnswerStatus answerStatus = (status.equals("accepted")) ? AnswerStatus.ACCEPTED : AnswerStatus.REJECTED;
        Request request = requestService.getRequestById(requestId);
        RequestAnswer answer = new RequestAnswer();
        answer.setStatus(answerStatus);
        answer.setDescription(description);
        answer.setRequest(request);
        answer.setDate(new Date());
        request.setAnswer(answer);
        request.setRequestStatus(RequestStatus.ANSWERED);
        requestAnswerService.save(answer);
        return "redirect:/admin/adminPanel";
    }

}

