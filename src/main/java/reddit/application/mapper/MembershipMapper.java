package reddit.application.mapper;

import reddit.application.dto.MembershipDto;
import reddit.application.models.Membership;

public class MembershipMapper {
    public static Membership mapToMembership(MembershipDto membershipDto) {
        return Membership.builder()
                .id(membershipDto.getId())
                .user(membershipDto.getUser())
                .community(membershipDto.getCommunity())
                .joinedOn(membershipDto.getJoinedOn())
                .build();
    }

    public static MembershipDto mapToMembershipDto(Membership membership) {
        return MembershipDto.builder()
                .id(membership.getId())
                .user(membership.getUser())
                .community(membership.getCommunity())
                .joinedOn(membership.getJoinedOn())
                .build();
    }
    


}
