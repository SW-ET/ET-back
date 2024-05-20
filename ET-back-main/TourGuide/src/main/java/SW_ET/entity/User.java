package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Entity
@Table(name="Users")
@Getter
@Setter
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_key_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
        private Long userKeyId;  // user_id를 user_key_id로 변경

        @Column(name = "user_id", nullable = false, unique = true, length = 255)  // 새로운 user_id 필드 추가
        private String userId;

/*        @Column(name = "user_name", nullable = false, unique = false, length = 255)
        private String userName;*/

        @Column(name = "user_Nickname", nullable = false, unique = false, length = 255)
        private String userNickName;

/*        @Column(name = "user_email", nullable = false, unique = true, length = 255)
        private String userEmail;*/

        @Column(name = "user_password", nullable = false, length = 255)
        private String userPassword;

        @Column(name = "regist_date", nullable = false)
        private LocalDate registDate = LocalDate.now();

        // 권한 필드 추가
        @Column(name = "user_role", nullable = true, length = 255)
        private String userRole;  // 예: "ROLE_USER ROLE_ADMIN"

}
