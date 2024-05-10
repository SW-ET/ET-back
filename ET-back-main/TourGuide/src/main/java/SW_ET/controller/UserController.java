package SW_ET.controller;

import SW_ET.dto.UserDto;
import SW_ET.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/register";
        }
        if (!userDto.getUserPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "users/register";
        }
        if (userService.isUserIdExists(userDto.getUserId())) {
            model.addAttribute("error", "User ID already exists.");
            return "users/register";
        }
        userService.registerUser(userDto);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDto userDto, HttpSession session, Model model) {
        if (userService.validateUser(userDto)) {
            session.setAttribute("user", userDto);
            return "redirect:/users/home";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "users/login";
        }
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("error", "Please login first.");
            return "users/login";
        }
        return "users/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
