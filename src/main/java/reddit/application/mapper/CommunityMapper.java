package reddit.application.mapper;

import java.util.stream.Collectors;

import reddit.application.dto.CommunityDto;
import reddit.application.models.Community;

import static reddit.application.mapper.PostMapper.mapToPostDto;
import static reddit.application.mapper.MembershipMapper.mapToMembershipDto;

public class CommunityMapper {
    public static Community mapToCommunity(CommunityDto communityDto) {
        Community community = Community.builder()
                .id(communityDto.getId())
                .name(communityDto.getName())
                .description(communityDto.getDescription())
                .communityPictureUrl(communityDto.getCommunityPictureUrl())
                .communityPictureHeaderUrl(communityDto.getCommunityPictureHeaderUrl())
                .createdOn(communityDto.getCreatedOn())
                .updatedOn(communityDto.getUpdatedOn())
                .createdBy(communityDto.getCreatedBy())
                .build();

        return community;

    }
    
    public static CommunityDto mapToCommunityDto(Community community) {
        CommunityDto communityDto = CommunityDto.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .communityPictureUrl(community.getCommunityPictureUrl())
                .communityPictureHeaderUrl(community.getCommunityPictureHeaderUrl())
                .createdOn(community.getCreatedOn())
                .updatedOn(community.getUpdatedOn())
                .createdBy(community.getCreatedBy())
                .posts(community.getPosts().stream().map((post) -> mapToPostDto(post)).collect(Collectors.toList()))
                .memberships(community.getMemberships().stream().map((membership) -> mapToMembershipDto(membership)).collect(Collectors.toList()))
                .build();
        return communityDto;
    }

}
