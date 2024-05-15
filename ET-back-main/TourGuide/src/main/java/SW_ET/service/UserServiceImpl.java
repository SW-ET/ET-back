package SW_ET.service;

import SW_ET.dto.LoginDto;
import SW_ET.dto.UserDto;
import SW_ET.entity.User;
import SW_ET.exceptions.UserServiceException;
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
    public String registerUser(UserDto userDto) {
        if (userRepository.existsByUserId(userDto.getUserId())) { // userId는 문자열로 존재여부 확인
            throw new UserServiceException("User ID '" + userDto.getUserId() + "' already exists.");
        }
        if (userRepository.existsByUserNickName(userDto.getUserNickName())) { // 닉네임 존재 여부 확인
            throw new UserServiceException("User Nickname '" + userDto.getUserNickName() + "' already exists.");
        }
        if (!userDto.getUserPassword().equals(userDto.getConfirmPassword())) { // 비밀번호 일치 여부 확인
            throw new UserServiceException("Passwords do not match.");
        }
        User newUser = userDto.toUser();
        newUser.setUserPassword(passwordEncoder.encode(userDto.getUserPassword())); // 비밀번호 암호화 후 저장
        userRepository.save(newUser);
        return "success";
    }

    @Override
    public boolean isUserIdExists(String userId) {
        return userRepository.existsByUserId(userId); // 문자열 userId로 존재 여부 확인
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null); // 문자열 userId로 유저 정보 조회
    }

    @Override
    public boolean isUserNickNameExists(String userNickName) {
        return userRepository.existsByUserNickName(userNickName); // 닉네임으로 존재 여부 확인
    }

    @Override
    public boolean validateUser(LoginDto loginDto) {
        User user = userRepository.findByUserId(loginDto.getUserId()).orElse(null);
        if (user == null || !passwordEncoder.matches(loginDto.getUserPassword(), user.getUserPassword())) {
            return false;
        }
        return true;
    }

}
