package SW_ET.controller;

import SW_ET.dto.UserDto;
import SW_ET.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller  // 컨트롤러 빈으로 등록
@RequestMapping("/users")  // 이 컨트롤러의 모든 매핑은 /users 경로 아래에 위치
@RequiredArgsConstructor  // 필요한 의존성 자동 주입 (생성자 주입)
public class UserController {

    private final UserService userService;

    // 회원가입 폼 페이지로의 GET 요청 처리
    @GetMapping("/register")
    public String registerForm() {
        return "register";  // 회원가입 폼 뷰 이름 반환
    }

    // 회원가입 처리 POST 요청
    @PostMapping("/register")
    public String register(UserDto userDto) {
        userService.registerUser(userDto);  // 서비스를 통한 사용자 등록
        return "redirect:/login";  // 로그인 페이지로 리디렉션
    }
}
