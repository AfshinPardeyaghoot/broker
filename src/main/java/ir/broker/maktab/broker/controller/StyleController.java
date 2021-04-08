package ir.broker.maktab.broker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StyleController {

    @GetMapping("/static/css/investor/createTradeRequest.css")
    public String createTradeRequestStyle() {
        return "../static/css/investor/createTradeRequest.css";
    }

    @GetMapping("/static/css/signUp.css")
    public String signUpStyle() {
        return "../static/css/signUp.css";
    }

    @GetMapping("/static/css/login.css")
    public String loginStyle() {
        return "../static/css/login.css";
    }

    @GetMapping("/static/css/investor/editInvestorInfo.css")
    public String editInvestorStyle() {
        return "../static/css/investor/editInvestorInfo.css";
    }

    @GetMapping("/static/css/investor/investorPanel.css")
    public String investorPanelStyle() {
        return "../static/css/investor/investorPanel.css";
    }

    @GetMapping("/static/css/investor/allRequest.css")
    public String investorAllRequestsStyle() {
        return "../static/css/investor/allRequest.css";
    }

    @RequestMapping("/static/css/investor/requestDetail.css")
    public String requestDetailsStyle() {
        return "../static/css/investor/requestDetail.css";
    }

    @RequestMapping("/static/css/manager/managerPanel.css")
    public String managerPanelStyle() {
        System.out.println("in css controller");
        return "../static/css/manager/managerPanel.css";
    }

    @GetMapping("/static/css/manager/createNewUser.css")
    public String createNewUserStyle() {
        return "../static/css/manager/createNewUser.css";
    }

    @GetMapping("/static/javaScript/showAllUsers.js")
    public String showAllUserScript() {
        return "../static/javaScript/showAllUsers.js";
    }

    @GetMapping("/static/css/manager/showAllUser.css")
    public String showAllUserStyle() {
        return "../static/css/manager/showAllUser.css";
    }

    @GetMapping("/static/css/manager/showAllRequests.css")
    public String showAllRequestsStyle() {
        return "../static/css/manager/showAllRequests.css";
    }

    @GetMapping("/static/javaScript/showAllRequests.js")
    public String showAllRequestsScript() {
        return "../static/javaScript/showAllRequests.js";
    }

    @GetMapping("/static/javaScript/adminPanel.js")
    public String adminPanelScript() {
        return "../static/javaScript/adminPanel.js";
    }

    @GetMapping("/static/css/admin/adminPanel.css")
    public String adminPanelStyle() {
        return "../static/css/admin/adminPanel.css";
    }

    @GetMapping("/static/css/admin/requestAnswer.css")
    public String requestAnswerStyle(){
        return "../static/css/admin/requestAnswer.css";
    }

    @GetMapping("/static/javaScript/investorAllRequests.js")
    public String investorAllRequestsScript(){
        return "../static/javaScript/investorAllRequests.js";
    }

    @GetMapping("/static/css/investor/showAnswer.css")
    public String showAnswerStyle(){
        return "../static/css/investor/showAnswer.css";
    }

    @GetMapping("/static/css/manager/requestSubject.css")
    public String requestSubjectStyle(){
        return "../static/css/manager/requestSubject.css";
    }

    @GetMapping("/static/javaScript/requestSubject.js")
    public String requestSubjectScript(){
        return "../static/javaScript/requestSubject.js";
    }

    @GetMapping("/static/css/investor/editRequest.css")
    public String investorEditRequestStyle(){
        return "../static/css/investor/editRequest.css";
    }

    @GetMapping("/static/css/investor/editPassword.css")
    public String investorEditPasswordStyle(){
        return "../static/css/investor/editPassword.css";
    }

    @GetMapping("/static/css/manager/editUserPass.css")
    public String managerEditUserPassStyle(){
        return "../static/css/manager/editUserPass.css";
    }

}
