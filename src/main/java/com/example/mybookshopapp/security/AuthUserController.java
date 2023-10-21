package com.example.mybookshopapp.security;

import com.example.mybookshopapp.controllers.AbstractHeaderFooterController;
import com.example.mybookshopapp.data.ContactChangeConfirmationEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.dto.*;
import com.example.mybookshopapp.errors.*;
import com.example.mybookshopapp.security.jwt.JWTUtil;
import com.example.mybookshopapp.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final TransactionService transactionService;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final CookieService cookieService;

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

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   @CookieValue(value = "cartContents", required = false) String cartContents,
                                                   @CookieValue(value = "postponedContents", required = false) String postponedContents,
                                                   @CookieValue(value = "viewedContents", required = false) String viewedContents,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        ContactConfirmationResponse loginResponse = userService.jwtLogin(payload);
        EmailUserDetails userDetails = (EmailUserDetails) (customUserDetailsService.loadUserByUsername(jwtUtil.extractUsername(loginResponse.getResult())));
        UserEntity user = userDetails.getUserEntity();
        bookService.mergeCartBooks(cookieService.getIntegerIds(cartContents), user.getId());
        bookService.mergePostponedBooks(cookieService.getIntegerIds(postponedContents), user.getId());
        bookService.mergeViewedBooks(cookieService.getIntegerIds(viewedContents), user.getId());
        cookieService.deleteAllCookies(request, response);
        response.addCookie(new Cookie("token", loginResponse.getResult()));
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        UserEntity user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("booksList", bookService.getPageOfBooksByUserStatus(user.getId(), "PAID", 0, 20));
        }
        return "my";
    }

    @GetMapping("/my/archive")
    public String handleMyArchive(Model model) {
        UserEntity user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("booksList", bookService.getPageOfBooksByUserStatus(user.getId(), "ARCHIVED", 0, 20));
        }
        return "myarchive";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        UserEntity user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("transactions", transactionService.getTransactionByUser(user, 0, 50, "desc"));
        }
        return "profile";
    }

    @PostMapping("/profile/change")
    public String handleProfileChange(@RequestParam(value = "name") String name,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "passwordReply") String passwordReply,
                                      RedirectAttributes redirectAttributes) {

        UserEntity user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:profile";
        }
        List<String> messages = new ArrayList<>();

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
    public ResponseEntity<ApiResponse> handleProfileEmailChange(@RequestBody ContactDto email) throws UserAlreadyExistException, ApiWrongParameterException, UserUnauthorizedException {
        UserEntity user = userService.getCurrentUser();
        userProfileService.initiateChangeEmailConfirmation(user, email.getContact());
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @GetMapping("/profile/change/email")
    public String handleProfileEmailChangeConfirm(@RequestParam(name = "key") String key,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletResponse httpServletResponse) {
        try {
            UserEntity newUser = userProfileService.confirmEmailChange(key);
            redirectAttributes.addFlashAttribute("profileMessage", "Email changed successfully");

            // Update jwt token
            httpServletResponse.addCookie(cookieService.getJwtTokenCookie(newUser));
        } catch (ContactConfirmationException | UserAlreadyExistException e) {
            redirectAttributes.addFlashAttribute("profileMessage", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/change/phone")
    public ResponseEntity<ApiResponse> handleProfilePhoneChange(@RequestBody ContactDto phone) throws UserAlreadyExistException, ApiWrongParameterException, UserUnauthorizedException {
        UserEntity user = userService.getCurrentUser();
        userProfileService.initiateChangePhoneConfirmation(user, phone.getContact());
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @GetMapping("/profile/change/phone")
    public String handleProfilePhoneChangeConfirm(@RequestParam(name = "key") String key,
                                                  RedirectAttributes redirectAttributes,
                                                  HttpServletResponse httpServletResponse) {
        try {
            UserEntity newUser = userProfileService.confirmPhoneChange(key);
            redirectAttributes.addFlashAttribute("profileMessage", "Phone changed successfully");

            // Update jwt token
            httpServletResponse.addCookie(cookieService.getJwtTokenCookie(newUser));
        } catch (ContactConfirmationException | UserAlreadyExistException e) {
            redirectAttributes.addFlashAttribute("profileMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
}
