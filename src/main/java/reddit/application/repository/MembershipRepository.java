package reddit.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import reddit.application.models.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByCommunityId(Long communityId);

    Membership findByUserId(Long userId);

    Membership findByUserIdAndCommunityId(Long userId, Long communityId);

    List<Membership> findAllByUserId(Long userId);

    List<Membership> findAllByCommunityId(Long communityId);
    

}
