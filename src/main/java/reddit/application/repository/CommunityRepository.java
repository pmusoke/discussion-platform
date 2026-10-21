package reddit.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import reddit.application.models.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByName(String url);
    @Query("SELECT c from Community c WHERE c.name LIKE CONCAT('%', :query, '%')")
    List<Community> searchCommunities(String query);

    Community findCommunityByName(String communityName);
    


}
