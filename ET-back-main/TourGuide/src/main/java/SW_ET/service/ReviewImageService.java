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
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewImageService {

    @Autowired
    private ReviewImageRepository reviewImageRepository;

    private final Path rootLocation = Paths.get("이미지를 저장할 경로");

    public List<ReviewImages> storeImages(List<MultipartFile> files, Long reviewId) throws IOException {
        List<ReviewImages> storedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                storedImages.add(storeImage(file, reviewId));
            }
        }
        return storedImages;
    }

    public ReviewImages storeImage(MultipartFile file, Long reviewId) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Cannot store empty file.");
        }
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            ReviewImages reviewImage = new ReviewImages();
            reviewImage.setImageUrl(rootLocation.toString() + "/" + file.getOriginalFilename()); // Use getOriginalFilename here
            reviewImage.setImagePath(rootLocation.toString());
            reviewImage.setFileName(file.getOriginalFilename());
            reviewImage.setContentType(file.getContentType());
            reviewImage.setFileSize(file.getSize());
            // Code to fetch and set Review object goes here

            return reviewImageRepository.save(reviewImage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
