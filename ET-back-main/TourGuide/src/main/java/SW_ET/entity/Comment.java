package SW_ET.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Review review; // The review (or main post) this comment is attached to

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", nullable = true, columnDefinition = "INT UNSIGNED")
    private Comment parentComment; // Reference to parent comment if this is a reply

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private User user;

    @Lob
    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "comment_date", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime commentDate;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> replies = new HashSet<>(); // Replies to this comment
}
