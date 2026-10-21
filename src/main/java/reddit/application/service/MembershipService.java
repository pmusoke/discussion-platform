package reddit.application.service;

import java.util.List;

import reddit.application.dto.MembershipDto;

public interface MembershipService {

    void createMembership(Long userId, Long communityId, MembershipDto membershipDto);

    MembershipDto findByUserIdAndCommunityId(Long userId, Long communityId);

    void deleteMembership(Long userId, Long communityId);

    List<MembershipDto> findAllByUserId(Long userId);

    List<MembershipDto> findAllByCommunityId(Long communityId);

}
