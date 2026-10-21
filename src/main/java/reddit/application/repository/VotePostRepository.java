package reddit.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reddit.application.models.VotePost;

public interface VotePostRepository extends JpaRepository<VotePost, Long> {

    VotePost findByUserIdAndPostId(Long userId, Long postId);

}
