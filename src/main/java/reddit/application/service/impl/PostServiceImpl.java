package reddit.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reddit.application.dto.MembershipDto;
import reddit.application.dto.PostDto;
import reddit.application.mapper.PostMapper;
import reddit.application.models.Community;
import reddit.application.models.Post;
import reddit.application.models.UserEntity;
import reddit.application.repository.CommunityRepository;
import reddit.application.repository.PostRepository;
import reddit.application.repository.UserRepository;
import reddit.application.service.MembershipService;
import reddit.application.service.PostService;

import static reddit.application.mapper.PostMapper.mapToPost;
import static reddit.application.mapper.PostMapper.mapToPostDto;


@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private CommunityRepository communityRepository;
    private UserRepository userRepository;
    private MembershipService membershipService;

    
    
    @Autowired
    public PostServiceImpl(PostRepository postRepository, CommunityRepository communityRepository,
            UserRepository userRepository, MembershipService membershipService) {
        this.postRepository = postRepository;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.membershipService = membershipService;
    }


    @Override
    public void createPost(Long communityId, Long userId, PostDto postDto) {
        Community community = communityRepository.findById(communityId).get();
        UserEntity user = userRepository.findById(userId).get();
        Post post = mapToPost(postDto);
        post.setCommunity(community);
        post.setCreatedBy(user);
        post.setVotes(0);
        postRepository.save(post);
        


    }

    @Override
    public List<PostDto> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto findByPostId(Long postId) {
        Post post = postRepository.findById(postId).get();
        return mapToPostDto(post);

    }

    @Override
    public void updatePost(PostDto postDto) {
        Post post = postRepository.findById(postDto.getId()).get();

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setPhotoUrl(postDto.getPhotoUrl());

        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostDto> findAllPostsByCommunityId(Long communityId) {
        List<Post> posts = postRepository.findAllPostsByCommunityId(communityId);
        return posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());

    }


    @Override
    public List<PostDto> findHomePosts(Long userId) {
        
        List<MembershipDto> memberships = membershipService.findAllByUserId(userId);

        return memberships.stream().flatMap(membership -> membership.getCommunity().getPosts().stream())
                .map(PostMapper::mapToPostDto).toList();
        
    }


    @Override
    public List<PostDto> findAllPostsByUserId(Long userId) {        
        List<Post> posts = postRepository.findAllPostsByCreatedById(userId);
        return posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());

    }


    @Override
    public List<PostDto> searchPosts(String query) {
        List<Post> posts = postRepository.searchPosts(query);
        return posts.stream().map(post -> mapToPostDto(post)).collect(Collectors.toList());
    }


}
