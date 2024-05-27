package SW_ET.controller;

import SW_ET.entity.ReviewImages;
import SW_ET.service.ReviewImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ReviewImageController {

    @Autowired
    private ReviewImageService reviewImageService;

    @PostMapping("/upload/{reviewId}")
    public ResponseEntity<ReviewImages> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable Long reviewId) {
        try {
            ReviewImages savedImage = reviewImageService.storeImage(file, reviewId);
            return ResponseEntity.ok(savedImage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
