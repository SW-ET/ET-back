package SW_ET.service;

import SW_ET.dto.CommentDto;
import SW_ET.dto.ReviewDto;
import SW_ET.entity.*;
import SW_ET.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LikeDislikeService likeDislikeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DestinationRepository destinationRepository;
    private final Path rootLocation = Paths.get("uploaded-images");

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }  // 권한 검증

    public void likeOrDislikeReview(Long reviewId, boolean like) {
        if (!isAuthenticated()) {
            throw new SecurityException("User must be authenticated to interact with reviews.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((User) authentication.getPrincipal()).getUserId();
        likeDislikeService.addLikeDislike(Long.parseLong(userId), "Review", reviewId, like);
    }

    // 리뷰 생성 및 이미지 저장
    public Review createReviewWithImages(ReviewDto reviewDto, List<MultipartFile> imageFiles) throws IOException {
        if (!isAuthenticated()) {
            throw new SecurityException("User must be authenticated to create reviews.");
        }

        User user = userRepository.findByUserId(reviewDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 여행지 ID 확인 및 기타 여행지 처리
        Destination destination = destinationRepository.findById(reviewDto.getDestinationId())
                .orElseGet(() -> destinationRepository.findByName("기타").orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destination not configured for '기타'")));

        Review review = new Review();
        review.setUser(user);
        review.setDestination(destination);
        review.setReviewTitle(reviewDto.getReviewTitle());
        if (reviewDto.getDestinationId() == null) {
            review.setReviewText(reviewDto.getReviewText() + " (기타 여행지 리뷰)");
        } else {
            review.setReviewText(reviewDto.getReviewText());
        }
        review.setDatePosted(reviewDto.getDatePosted());
        review.setTags(reviewDto.getTags());
        review.setUseYn(true);
        review.setDeleted(reviewDto.getIsDeleted());

        review = reviewRepository.save(review);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                ReviewImages image = storeImage(file, review);
                imageRepository.save(image);
            }
        }

        return review;
    }

    // 리뷰 업데이트
    public Review updateReview(Long reviewId, ReviewDto reviewDto) throws IOException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        review.setTags(reviewDto.getTags());
        review.setUseYn(reviewDto.getUseYn());
        review.setDeleted(reviewDto.getIsDeleted());
        review.setDatePosted(reviewDto.getDatePosted()); // Optionally update the date

        return reviewRepository.save(review);
    }

    // 리뷰 삭제.
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found")); // 리뷰를 삭제하기전에 리뷰가 존재하지않는 경우.

        review.setDeleted(true); // 삭제됨 플래그를 true로 설정.
        review.setDeletedTime(LocalDateTime.now());  // Optionally, set the deletion time
        reviewRepository.save(review); // 리뷰를 물리적으로 제거하지 않음. db내에 데이터 보존.
    }

    // 이미지 저장 메소드
    private ReviewImages storeImage(MultipartFile file, Review review) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        Path destinationFile = rootLocation.resolve(
                        Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new IOException("Cannot store file outside current directory.");
        }
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile);
            ReviewImages image = new ReviewImages();
            image.setFileName(destinationFile.getFileName().toString());
            image.setContentType(file.getContentType());
            image.setFileSize(file.getSize());
            image.setReview(review);
            image.setImageUrl("/images/" + destinationFile.getFileName().toString());
            image.setImagePath(destinationFile.toString());
            return image;
        }
    }

    // 댓글메소드
    public Comment postComment(CommentDto commentDto) {
        Review review = reviewRepository.findById(commentDto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = new Comment();
        comment.setReview(review);
        comment.setUser(user);
        comment.setCommentText(commentDto.getCommentText());
        comment.setCommentDate(LocalDateTime.now()); // Setting current time as comment date

        return commentRepository.save(comment);
    }

    // 댓글에 대댓글 달기
    public Comment replyToComment(Long parentCommentId, String commentText, Long userId) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));

        Comment reply = new Comment();
        reply.setParentComment(parentComment);
        reply.setCommentText(commentText);
        reply.setUser(userRepository.findById(userId).orElseThrow());
        reply.setCommentDate(LocalDateTime.now());

        return commentRepository.save(reply);
    }

    // 제목으로 리뷰 찾기.
    public List<Review> searchReviewsByTitle(String title) {
        return reviewRepository.findByReviewTitleContaining(title);
    }
}
