package reddit.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reddit.application.dto.CommunityDto;
import reddit.application.dto.MembershipDto;
import reddit.application.mapper.CommunityMapper;
import reddit.application.models.Community;
import reddit.application.models.UserEntity;
import reddit.application.repository.CommunityRepository;
import reddit.application.repository.UserRepository;
import reddit.application.security.SecurityUtil;
import reddit.application.service.CommunityService;
import reddit.application.service.MembershipService;

import static reddit.application.mapper.CommunityMapper.mapToCommunityDto;
import static reddit.application.mapper.CommunityMapper.mapToCommunity;

@Service
public class CommunityServiceImpl implements CommunityService {
    private CommunityRepository communityRepository;
    private UserRepository userRepository;
    private MembershipService membershipService;

    @Autowired
    public CommunityServiceImpl(CommunityRepository communityRepository, UserRepository userRepository, MembershipService membershipService) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.membershipService = membershipService;
    }

    @Override
    public List<CommunityDto> findAllCommunities() {
        List<Community> communities = communityRepository.findAll();
        return communities.stream().map((community) -> mapToCommunityDto(community)).collect(Collectors.toList());
    }

    @Override
    public Community saveCommunity(CommunityDto communityDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        if (communityDto.getCommunityPictureHeaderUrl() == "") {
            communityDto.setCommunityPictureHeaderUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLlyppVE6w5ehU_UpX6zofzAb_tDbsKVaU06ceignN9A&s=10");
        }

        if (communityDto.getCommunityPictureUrl() == "") {
            communityDto.setCommunityPictureUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRB941uFLFgF0EUslvtQKNOUYv2AHD8qPl_r4-wf9mqt7HXpMuTMJrhtvQJ&s=10");
        }


        Community community = mapToCommunity(communityDto);
        community.setCreatedBy(user);

        communityRepository.save(community);

        MembershipDto membershipDto = new MembershipDto();

        membershipService.createMembership(user.getId(), community.getId(), membershipDto);

        return community;
    }

    @Override
    public CommunityDto findCommunityById(Long communityId) {
        Community community = communityRepository.findById(communityId).get();
        return mapToCommunityDto(community);
    }

    public void updateCommunity(String communityName, CommunityDto communityDto) {
        Community community = communityRepository.findCommunityByName(communityName);

        community.setName(communityDto.getName());
        community.setDescription(communityDto.getDescription());
        community.setCommunityPictureUrl(communityDto.getCommunityPictureUrl());
        community.setCommunityPictureHeaderUrl(communityDto.getCommunityPictureHeaderUrl());
        communityRepository.save(community);
    }

    @Override
    public void deleteCommunity(Long communityId) {
        communityRepository.deleteById(communityId);
    }

    @Override
    public List<CommunityDto> searchCommunities(String query) {
        List<Community> communities = communityRepository.searchCommunities(query);
        return communities.stream().map(community -> mapToCommunityDto(community)).collect(Collectors.toList());
    }

    @Override
    public CommunityDto findCommunityByName(String communityName) {
        Community community = communityRepository.findCommunityByName(communityName);
        return mapToCommunityDto(community);
    }

    @Override
    public List<CommunityDto> findAllCommunitiesByUserId(Long userId) {
        List<MembershipDto> memberships = membershipService.findAllByUserId(userId);

        return memberships.stream().map(membership -> membership.getCommunity()).map(CommunityMapper::mapToCommunityDto)
                .toList();

    }

    @Override
    public void delete(String communityName) {
        Community community = communityRepository.findCommunityByName(communityName);

        communityRepository.deleteById(community.getId());
    }
    




    

}
