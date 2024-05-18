package SW_ET.controller;

import SW_ET.dto.LoginDto;
import SW_ET.dto.UserDto;
import SW_ET.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;

@Controller
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations pertaining to user management in the application")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Show Registration Form")
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "users/register";
    }

    @Operation(summary = "Register a new User")
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors occurred: " + result.getAllErrors());
        }
        try {
            userService.registerUser(userDto);
            // 사용자 등록이 성공하면 로그인 페이지로 리디렉션합니다.
            return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/users/login_proc")).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Show Login Form")
    @GetMapping("/login")
    public String showLoginForm() {
        return "login_proc";
    }

    @Operation(summary = "Login a User")
    @PostMapping("/login_proc")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        boolean isAuthenticated = userService.validateUser(loginDto);
        if (!isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials"));
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Login successful"));
    }

    @Operation(summary = "Show Home Page")
    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("error", "Please login first.");
            return "login_proc";
        }
        return "users/home";
    }

    @Operation(summary = "Logout a User")
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @Operation(summary = "Check UserName")
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = userService.isUserIdExists(username);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Check UserNickname")
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameExists(@RequestParam String nickname) {
        boolean exists = userService.isUserNickNameExists(nickname);
        return ResponseEntity.ok(exists);
    }
}