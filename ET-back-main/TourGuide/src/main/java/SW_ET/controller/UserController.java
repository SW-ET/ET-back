package SW_ET.controller;

import SW_ET.dto.UserDto;
import SW_ET.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 폼을 보여주는 메서드
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto()); // UserDto 객체를 모델에 추가하여 폼 바인딩을 위해 전달
        return "register"; // 'src/main/resources/templates/register.html'에 매핑됩니다.
    }

    // 회원가입 처리 메서드
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDto userDto) {
        System.out.println("Received user: " + userDto); // 받은 사용자 데이터를 콘솔에 출력
        userService.registerUser(userDto); // UserService를 통해 사용자 등록
        return "redirect:/login"; // 등록 후 로그인 페이지로 리다이렉트
    }
}
