package com.example.mybookshopapp.security;

import com.example.mybookshopapp.controllers.AbstractHeaderFooterController;
import com.example.mybookshopapp.data.SmsCodeEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.errors.UserAlreadyExistException;
import com.example.mybookshopapp.services.BookService;
import com.example.mybookshopapp.services.TransactionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthUserController extends AbstractHeaderFooterController {

    private final BookstoreUserRegister userRegister;
    private final BookService bookService;
    private final CodeService codeService;
    private final TransactionService transactionService;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister, BookService bookService, CodeService codeService, TransactionService transactionService) {
        this.userRegister = userRegister;
        this.bookService = bookService;
        this.codeService = codeService;
        this.transactionService = transactionService;
    }

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/api/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");

        // Confirmation with email
        if (payload.getContact().contains("@")) {
            String codeString = codeService.sendCodeToEmail(payload.getContact());

            // Expires in 60 seconds
            codeService.saveCode(new SmsCodeEntity(codeString, 60));
            return response;

            // Confirmation with phone
        } else {
            String smsCodeString = codeService.sendCodeToPhone(payload.getContact());

            // Expires in 60 seconds
            codeService.saveCode(new SmsCodeEntity(smsCodeString, 60));
            return response;
        }
    }

    @PostMapping("/api/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();

        if (codeService.verifyCode(payload.getCode())) {
            response.setResult("true");
        }
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) throws UserAlreadyExistException {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user != null) {
            model.addAttribute("booksList", bookService.getPageOfBooksByUserStatus(user.getId(), "PAID", 0, 20));
        }
        return "my";
    }

    @GetMapping("/my/archive")
    public String handleMyArchive(Model model) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user != null) {
            model.addAttribute("booksList", bookService.getPageOfBooksByUserStatus(user.getId(), "ARCHIVED", 0, 20));
        }
        return "myarchive";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user != null){
            model.addAttribute("transactions", transactionService.getTransactionsByUserAsc(user, 0, 5));
        }
        return "profile";
    }

    @PostMapping("/profile/change")
    public String handleProfileChange(@RequestParam(value = "name") String name,
                                      @RequestParam(value = "mail") String email,
                                      @RequestParam(value = "phone") String phone,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "passwordReply") String passwordReply,
                                      RedirectAttributes redirectAttributes){

        UserEntity user = (UserEntity) userRegister.getCurrentUser();

        if (user != null){
            if (!password.isEmpty() && !passwordReply.isEmpty() && password.equals(passwordReply)){
                userRegister.changePassword(user, password);
                redirectAttributes.addFlashAttribute("profileMessage", "Profile successfully changed");
            } else {
                redirectAttributes.addFlashAttribute("profileMessage", "Password do not match or empty");
            }
        }
        return "redirect:/profile";
    }
}
