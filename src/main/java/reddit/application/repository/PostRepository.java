package reddit.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import reddit.application.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String url);
    @Query("SELECT p from Post p WHERE p.title LIKE CONCAT('%', :query, '%')")
    List<Post> searchPosts(String query);

    List<Post> findAllPostsByCommunityId(Long communityId);

    List<Post> findAllPostsByCreatedById(Long userId);

}
