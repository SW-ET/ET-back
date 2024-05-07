package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="Users")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
        private Long userId;

        @Column(name = "user_name", nullable = false, unique = true, length = 255)
        private String userName;

        @Column(name = "user_email", nullable = false, unique = true, length = 255)
        private String userEmail;

        @Column(name = "user_password", nullable = false, length = 255)
        private String userPassword;

        @Column(name = "regist_date", nullable = false)
        private LocalDate registDate = LocalDate.now();
}
