package reddit.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import reddit.application.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByComment(String url);
    @Query("SELECT c from Comment c WHERE c.comment LIKE CONCAT('%', :query, '%')")
    List<Comment> searchComment(String query);

    List<Comment> findByPostId(Long postId);

    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);

    List<Comment> findByCreatedById(Long userId);

}
