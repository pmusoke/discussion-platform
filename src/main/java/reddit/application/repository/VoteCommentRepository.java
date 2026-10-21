package reddit.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reddit.application.models.VoteComment;

public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {
    
    VoteComment findByUserIdAndCommentId(Long userId, Long commentId);

}
