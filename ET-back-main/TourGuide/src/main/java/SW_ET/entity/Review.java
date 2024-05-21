package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = true, columnDefinition = "INT UNSIGNED")
    private Destination destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = true, columnDefinition = "INT UNSIGNED")
    private Festival festival;

    @Column(name = "review_title", nullable = false, length = 255)
    private String reviewTitle;

    @Lob
    @Column(name = "review_text", nullable = false)
    private String reviewText;

    @Column(name = "date_posted", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime datePosted;

    @Column(name = "review_date_modi", nullable = true)
    private LocalDateTime reviewDateModi;

    @Column(name = "likes", nullable = false)
    private Long likeNumber;

    @Column(name = "dislikes", nullable = false)
    private Long dislikeNumber;

    @Column(name = "tags", nullable = true)
    private String tags;

    @Column(name = "use_yn", nullable = false, length = 1)
    private String useYn;

    @Column(name = "deleted_time", nullable = true)
    private LocalDateTime deletedTime;

    @Column(name = "deleted_true", nullable = false, length = 1)
    private String deletedTrue;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Image> images;
}