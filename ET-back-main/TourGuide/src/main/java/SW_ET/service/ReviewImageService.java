package SW_ET.service;

import SW_ET.entity.ReviewImages;
import SW_ET.repository.ReviewImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ReviewImageService {

    @Autowired
    private ReviewImageRepository reviewImageRepository;

    private final Path rootLocation = Paths.get("이미지를 저장할 경로");

    public ReviewImages storeImage(MultipartFile file, Long reviewId) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("비어있는 파일을 저장할 수 없습니다.");
        }
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            ReviewImages reviewImage = new ReviewImages();
            reviewImage.setImageUrl(rootLocation.toString() + "/" + file.getOriginalFilename());
            reviewImage.setImagePath(rootLocation.toString());
            reviewImage.setFileName(file.getOriginalFilename());
            reviewImage.setContentType(file.getContentType());
            reviewImage.setFileSize(file.getSize());

            // reviewId와 userId 설정 필요
            // reviewImage.setReview(review);
            // reviewiumage.setUser(user);

            return reviewImageRepository.save(reviewImage);
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}