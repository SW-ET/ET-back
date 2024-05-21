package SW_ET.controller;

import SW_ET.config.JwtTokenProvider;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private AuthenticationManager authenticationManager; // Ensure this bean is configured in your security configuration.
    @Autowired
    private JwtTokenProvider jwtTokenProvider; // This should be your JWT utility class that generates the token.

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
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/users/login_proc")).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @Operation(summary = "Show Login Form")
    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login_proc";
    }

    @Operation(summary = "Login a User")
    @PostMapping("/login_proc")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getUserPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(Collections.singletonMap("jwt", jwt)); // Send JWT as a response
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @Operation(summary = "Show Home Page")
    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("error", "Please login first.");
            return "users/login_proc";
        }
        return "users/home";
    }

    @Operation(summary = "Logout a User")
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @Operation(summary = "Check if UserId exists")
    @GetMapping("/check-userId")
    public ResponseEntity<Boolean> checkUserIdExists(@RequestParam("userId") String userId) {
        boolean exists = userService.isUserIdExists(userId);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Check if UserNickname exists")
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkUserNicknameExists(@RequestParam("userNickName") String userNickName) {
        boolean exists = userService.isUserNickNameExists(userNickName);
        return ResponseEntity.ok(exists);
    }
}
