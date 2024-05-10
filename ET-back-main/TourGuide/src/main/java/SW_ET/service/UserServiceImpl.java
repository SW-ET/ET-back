package SW_ET.service;

import SW_ET.dto.UserDto;
import SW_ET.entity.User;
import SW_ET.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean isUserIdExists(String userId) {
        return userRepository.existsById(userId);  // String 타입으로 바로 사용
    }

    @Override
    public String registerUser(UserDto userDto) {
        if (isUserIdExists(userDto.getUserId())) {
            return "User ID already exists.";
        }
        if (!userDto.getUserPassword().equals(userDto.getConfirmPassword())) {
            return "Passwords do not match.";
        }
        User newUser = userDto.toUser();  // UserDto to User 변환 필요
        newUser.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        userRepository.save(newUser);
        return "success";
    }

    @Override
    public boolean validateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getUserId()).orElse(null);  // String 타입 사용
        if (user != null && passwordEncoder.matches(userDto.getUserPassword(), user.getUserPassword())) {
            return true;
        }
        return false;
    }
}
