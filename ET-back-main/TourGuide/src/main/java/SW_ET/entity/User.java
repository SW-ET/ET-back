package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="Users")
@Getter
@Setter
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)   //자동 증가 설정
        @Column(name = "user_id", nullable = false, columnDefinition = "INT UNSIGNED")
        private Long id;  // 사용자 ID (기본 키)

        @Column(name = "user_name", nullable = false, unique = true, length = 255)
        private String username;  //사용자 이름 , 유니크 키 지정 해서 중복 x

        @Column(name = "email", nullable = false, unique = true, length = 255)
        private String email;  //사용자 이메일

        @Column(name = "password", nullable = false, length = 255)
        private String password;  //사용자 비밀번호

        @Column(name = "regist_date", nullable = false, columnDefinition = "DATE DEFAULT CURDATE()")
        // 가입일자를 자동으로 현재 날짜 저장.
        private LocalDate registDate;  //가입 일자

}