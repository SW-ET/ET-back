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
            return ResponseEntity.ok("User successfully registered. Redirecting to login...");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Show Login Form")
    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login_proc";
    }

    @Operation(summary = "Login a User")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors occurred: " + result.getAllErrors());
        }
        if (!userService.validateUser(loginDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
        session.setAttribute("user", loginDto);
        return ResponseEntity.ok("User successfully logged in. Redirecting to home page...");
    }


    @Operation(summary = "Show Home Page")
    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("error", "Please login first.");
            return "users/login";
        }
        return "users/home";
    }

    @Operation(summary = "Logout a User")
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}