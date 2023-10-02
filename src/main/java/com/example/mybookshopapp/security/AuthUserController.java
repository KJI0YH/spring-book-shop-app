package com.example.mybookshopapp.security;

import com.example.mybookshopapp.controllers.AbstractHeaderFooterController;
import com.example.mybookshopapp.data.ApiResponse;
import com.example.mybookshopapp.data.ContactChangeConfirmationEntity;
import com.example.mybookshopapp.data.SmsCodeEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.ContactConfirmationPayload;
import com.example.mybookshopapp.dto.ContactConfirmationResponse;
import com.example.mybookshopapp.dto.ContactDto;
import com.example.mybookshopapp.dto.RegistrationForm;
import com.example.mybookshopapp.errors.ContactConfirmationException;
import com.example.mybookshopapp.errors.UserAlreadyExistException;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import com.example.mybookshopapp.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthUserController extends AbstractHeaderFooterController {

    private final BookstoreUserRegister userRegister;
    private final BookService bookService;
    private final CodeService codeService;
    private final TransactionService transactionService;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final JWTUtil jwtUtil;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final ContactChangeConfirmationService confirmationService;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister, BookService bookService, CodeService codeService, TransactionService transactionService, EmailService emailService, PhoneService phoneService, JWTUtil jwtUtil, BookstoreUserDetailsService bookstoreUserDetailsService, ContactChangeConfirmationService confirmationService) {
        this.userRegister = userRegister;
        this.bookService = bookService;
        this.codeService = codeService;
        this.transactionService = transactionService;
        this.emailService = emailService;
        this.phoneService = phoneService;
        this.jwtUtil = jwtUtil;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.confirmationService = confirmationService;
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
        if (user == null){
            return "redirect:profile";
        }
        List<String> messages = new ArrayList<String>();

        // Name change
        if (!name.isEmpty() && !name.equals(user.getName())){
            userRegister.changeName(user, name);
            messages.add("Name successfully changed");
        }

        // Password change
        if (!password.isEmpty() && !passwordReply.isEmpty()) {
            if (password.equals(passwordReply)) {
                userRegister.changePassword(user, password);
                messages.add("Password successfully changed");
            } else {
                messages.add("Password do not match");
            }
        }

        redirectAttributes.addFlashAttribute("profileMessage", messages);

        return "redirect:/profile";
    }

    @PostMapping("/profile/change/email")
    public ResponseEntity<ApiResponse> handleProfileEmailChange(@RequestBody ContactDto email){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newEmail = email.getContact();

        if (!newEmail.isEmpty() && !user.getEmail().equals(newEmail)){
            String key = emailService.generateEmailConfirmationKey(newEmail);
            confirmationService.createConfirmation(key, user, newEmail);
            String link = emailService.getEmailConfirmationLink(key);
            emailService.sendEmailMessage(newEmail, "Bookstore email change confirmation", "Click on the link to confirm your email: " + link);
        }

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
    }

    @GetMapping("/profile/change/email")
    public String handleProfileEmailChangeConfirm(@RequestParam(name = "key", required = true) String key,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletResponse httpServletResponse){
        ContactChangeConfirmationEntity newContact;
        try {
            newContact = confirmationService.getContactConfirmationByKey(key);
        } catch(ContactConfirmationException e){
            return "redirect:/profile";
        }

        UserEntity newUser = userRegister.changeEmail(newContact.getUser(), newContact.getContact());
        confirmationService.confirmContactChange(newContact);
        redirectAttributes.addFlashAttribute("profileMessage", "Email changed successfully");

        // Update jwt token
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(newUser.getEmail());
        Cookie cookie = new Cookie("token", jwtUtil.generateToken(userDetails));
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return "redirect:/profile";
    }

    @PostMapping("/profile/change/phone")
    public ResponseEntity<ApiResponse> handleProfilePhoneChange(@RequestBody ContactDto phone){
        UserEntity user = (UserEntity) userRegister.getCurrentUser();
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newPhone = phone.getContact();

        if (!newPhone.isEmpty() && !user.getPhone().equals(newPhone)){
            String key = phoneService.generatePhoneConfirmationKey(newPhone);
            confirmationService.createConfirmation(key, user, newPhone);
            String link = phoneService.getPhoneConfirmationLink(key);
            phoneService.sendPhoneMessage(newPhone, "Follow the link to confirm your phone change: " + link);
        }

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, true));
    }

    @GetMapping("/profile/change/phone")
    public String handleProfilePhoneChangeConfirm(@RequestParam(name = "key", required = true) String key,
                                                 RedirectAttributes redirectAttributes,
                                                  HttpServletResponse httpServletResponse){
        ContactChangeConfirmationEntity newContact;
        try {
            newContact = confirmationService.getContactConfirmationByKey(key);
        } catch (ContactConfirmationException e){
            return "redirect:/profile";
        }

        UserEntity newUser = userRegister.changePhone(newContact.getUser(), newContact.getContact());
        confirmationService.confirmContactChange(newContact);
        redirectAttributes.addFlashAttribute("profileMessage", "Phone changed successfully");


        // Update jwt token
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(newUser.getPhone());
        Cookie cookie = new Cookie("token", jwtUtil.generateToken(userDetails));
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return "redirect:/profile";
    }
}
