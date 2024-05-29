package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private User user;

    @Column(name = "review_title", nullable = false, length = 255)
    private String reviewTitle;

    @Lob
    @Column(name = "review_text", nullable = false)
    private String reviewText;

    @Column(name = "date_posted", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime datePosted;

    @Column(name = "review_date_modi", nullable = true)
    private LocalDateTime reviewDateModi;

    @Column(name = "use_yn", nullable = false, length = 1)
    private boolean useYn;  // Modified from boolean to String based on your database design needs.

    @Column(name = "deleted_time", nullable = true)
    private LocalDateTime deletedTime;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;  // Field renamed for clarity and initialized as false by default.

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ReviewImages> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewImages> reviewImages;

}
