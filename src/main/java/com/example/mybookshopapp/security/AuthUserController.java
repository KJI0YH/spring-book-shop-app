package com.example.mybookshopapp.security;

import com.example.mybookshopapp.controllers.AbstractHeaderFooterController;
import com.example.mybookshopapp.data.ContactChangeConfirmationEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.errors.ApproveContactException;
import com.example.mybookshopapp.errors.ContactConfirmationException;
import com.example.mybookshopapp.errors.UserAlreadyExistException;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import com.example.mybookshopapp.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthUserController extends AbstractHeaderFooterController {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final BookService bookService;
    private final ApproveContactService approveContactService;
    private final TransactionService transactionService;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final ContactChangeConfirmationService confirmationService;

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
    public ResponseEntity<ApiResponse> handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) throws ApproveContactException {
        userProfileService.initiateContactConfirmation(payload);
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping("/api/approveContact")
    @ResponseBody
    public ResponseEntity<ApiResponse> handleApproveContact(@RequestBody ContactConfirmationPayload payload) throws ApproveContactException {
        userProfileService.approveContact(payload);
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) throws UserAlreadyExistException {
        userService.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    // TODO create viewed cookie
    // TODO merge cookies cart, postponed, viewed
    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userService.jwtLogin(payload);
        httpServletResponse.addCookie(new Cookie("token", loginResponse.getResult()));
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("booksList", bookService.getPageOfBooksByUserStatus(user.getId(), "PAID", 0, 20));
        }
        return "my";
    }

    @GetMapping("/my/archive")
    public String handleMyArchive(Model model) {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("booksList", bookService.getPageOfBooksByUserStatus(user.getId(), "ARCHIVED", 0, 20));
        }
        return "myarchive";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("transactions", transactionService.getTransactionByUser(user, 0, 50, "desc"));
        }
        return "profile";
    }

    @PostMapping("/profile/change")
    public String handleProfileChange(@RequestParam(value = "name") String name,
                                      @RequestParam(value = "mail") String email,
                                      @RequestParam(value = "phone") String phone,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "passwordReply") String passwordReply,
                                      RedirectAttributes redirectAttributes) {

        UserEntity user = (UserEntity) userService.getCurrentUser();
        if (user == null) {
            return "redirect:profile";
        }
        List<String> messages = new ArrayList<String>();

        // Name change
        if (!name.isEmpty() && !name.equals(user.getName())) {
            userService.changeName(user, name);
            messages.add("Name successfully changed");
        }

        // Password change
        if (!password.isEmpty() && !passwordReply.isEmpty()) {
            if (password.equals(passwordReply)) {
                userService.changePassword(user, password);
                messages.add("Password successfully changed");
            } else {
                messages.add("Password do not match");
            }
        }

        redirectAttributes.addFlashAttribute("profileMessage", messages);

        return "redirect:/profile";
    }

    @PostMapping("/profile/change/email")
    public ResponseEntity<ApiResponse> handleProfileEmailChange(@RequestBody ContactDto email) {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newEmail = email.getContact();

        if (!newEmail.isEmpty() && !user.getEmail().equals(newEmail)) {
            String key = emailService.generateEmailConfirmationKey(newEmail);
            confirmationService.createConfirmation(key, user, newEmail);
            String link = emailService.getEmailConfirmationLink(key);
            emailService.sendEmailMessage(newEmail, "Bookstore email change confirmation", "Click on the link to confirm your email: " + link);
        }

        return ResponseEntity.ok(new ApiResponse(true));
    }

    @GetMapping("/profile/change/email")
    public String handleProfileEmailChangeConfirm(@RequestParam(name = "key", required = true) String key,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletResponse httpServletResponse) {
        ContactChangeConfirmationEntity newContact;
        try {
            newContact = confirmationService.getContactConfirmationByKey(key);
        } catch (ContactConfirmationException e) {
            return "redirect:/profile";
        }

        UserEntity newUser = userService.changeEmail(newContact.getUser(), newContact.getContact());
        confirmationService.confirmContactChange(newContact);
        redirectAttributes.addFlashAttribute("profileMessage", "Email changed successfully");

        // Update jwt token
        EmailUserDetails userDetails = (EmailUserDetails) customUserDetailsService.loadUserByUsername(newUser.getEmail());
        Cookie cookie = new Cookie("token", jwtUtil.generateToken(userDetails));
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return "redirect:/profile";
    }

    @PostMapping("/profile/change/phone")
    public ResponseEntity<ApiResponse> handleProfilePhoneChange(@RequestBody ContactDto phone) {
        UserEntity user = (UserEntity) userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newPhone = phone.getContact();

        if (!newPhone.isEmpty() && !user.getPhone().equals(newPhone)) {
            String key = phoneService.generatePhoneConfirmationKey(newPhone);
            confirmationService.createConfirmation(key, user, newPhone);
            String link = phoneService.getPhoneConfirmationLink(key);
            phoneService.sendPhoneMessage(newPhone, "Follow the link to confirm your phone change: " + link);
        }

        return ResponseEntity.ok(new ApiResponse(true));
    }

    @GetMapping("/profile/change/phone")
    public String handleProfilePhoneChangeConfirm(@RequestParam(name = "key", required = true) String key,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletResponse httpServletResponse) {
        ContactChangeConfirmationEntity newContact;
        try {
            newContact = confirmationService.getContactConfirmationByKey(key);
        } catch (ContactConfirmationException e) {
            return "redirect:/profile";
        }

        UserEntity newUser = userService.changePhone(newContact.getUser(), newContact.getContact());
        confirmationService.confirmContactChange(newContact);
        redirectAttributes.addFlashAttribute("profileMessage", "Phone changed successfully");


        // Update jwt token
        EmailUserDetails userDetails = (EmailUserDetails) customUserDetailsService.loadUserByUsername(newUser.getPhone());
        Cookie cookie = new Cookie("token", jwtUtil.generateToken(userDetails));
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return "redirect:/profile";
    }
}
