package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @Column(name = "item_type", nullable = false)
    private String itemType;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ratingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false)
    private User user;
}
