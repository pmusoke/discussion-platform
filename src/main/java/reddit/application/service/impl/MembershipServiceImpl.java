package reddit.application.service.impl;

import static reddit.application.mapper.MembershipMapper.mapToMembership;
import static reddit.application.mapper.MembershipMapper.mapToMembershipDto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import reddit.application.dto.MembershipDto;
import reddit.application.models.Community;
import reddit.application.models.Membership;
import reddit.application.models.UserEntity;
import reddit.application.repository.CommunityRepository;
import reddit.application.repository.MembershipRepository;
import reddit.application.repository.UserRepository;
import reddit.application.service.MembershipService;

@Service
public class MembershipServiceImpl implements MembershipService {
    private MembershipRepository membershipRepository;
    private CommunityRepository communityRepository;
    private UserRepository userRepository;

    public MembershipServiceImpl(MembershipRepository membershipRepository, CommunityRepository communityRepository,
            UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createMembership(Long userId, Long communityId, MembershipDto membershipDto) {
        // TODO Auto-generated method stub
        UserEntity user = userRepository.findById(userId).get();
        Community community = communityRepository.findById(communityId).get();
        Membership membership = mapToMembership(membershipDto);
        membership.setUser(user);
        membership.setCommunity(community);
        membershipRepository.save(membership);
    }

    @Override
    public MembershipDto findByUserIdAndCommunityId(Long userId, Long communityId) {
        // TODO Auto-generated method stub
        Membership membership = membershipRepository.findByUserIdAndCommunityId(userId, communityId);

        if (membership == null) {
            return null;
        }


        return mapToMembershipDto(membership);


    }

    @Override
    public void deleteMembership(Long userId, Long communityId) {
        // TODO Auto-generated method stub

        Membership membership = membershipRepository.findByUserIdAndCommunityId(userId, communityId);


        membershipRepository.deleteById(membership.getId());
    }

    @Override
    public List<MembershipDto> findAllByUserId(Long userId) {
        List<Membership> memberships = membershipRepository.findAllByUserId(userId);
        return memberships.stream().map(membership -> mapToMembershipDto(membership)).collect(Collectors.toList());
    }

    @Override
    public List<MembershipDto> findAllByCommunityId(Long communityId) {
        List<Membership> memberships = membershipRepository.findAllByCommunityId(communityId);
        return memberships.stream().map(membership -> mapToMembershipDto(membership)).collect(Collectors.toList());
    }

    

    
    

}
