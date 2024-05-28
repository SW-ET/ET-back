package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "like_dislikes")
public class LikeDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false)
    private User user;

    @Column(name = "item_type", nullable = false)
    private String itemType; // "Review", "Destination" 등

    @Column(name = "item_id", nullable = false)
    private Long itemId; // 각 항목의 ID

    @Column(name = "liked")
    private Boolean liked; // true for like, false for dislike, null if no action

}