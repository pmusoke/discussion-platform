package reddit.application.service;

import java.util.List;

import reddit.application.dto.CommunityDto;
import reddit.application.models.Community;

public interface CommunityService {
    List<CommunityDto> findAllCommunities();

    Community saveCommunity(CommunityDto communityDto);

    CommunityDto findCommunityById(Long communityId);

    void updateCommunity(String communityName, CommunityDto communityDto);

    void deleteCommunity(Long communityId);

    List<CommunityDto> searchCommunities(String query);

    CommunityDto findCommunityByName(String communityName);

    List<CommunityDto> findAllCommunitiesByUserId(Long userId);

    void delete(String communityName);
    
    

}
