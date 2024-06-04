package SW_ET.service;/*
package SW_ET.service;

import SW_ET.dto.CommentDto;
import SW_ET.entity.Comment;
import SW_ET.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(CommentDto commentDto) {
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required to post comments.");
        }

        Comment comment = new Comment();
        comment.setReview(commentDto.getReview());
        comment.setUser(commentDto.getUser());
        comment.setCommentText(commentDto.getCommentText());
        comment.setCommentDate(commentDto.getCommentDate());
        comment.setParentComment(commentDto.getParentComment());

        return commentRepository.save(comment);
    }

    // Additional methods to retrieve and delete comments can be added here
}
*/
