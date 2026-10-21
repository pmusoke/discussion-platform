package reddit.application.service;

import java.util.List;

import reddit.application.dto.PostDto;

public interface PostService {
    void createPost(Long communityId, Long userId, PostDto postDto);

    List<PostDto> findAllPosts();

    PostDto findByPostId(Long postId);

    void updatePost(PostDto postDto);

    void deletePost(Long postId);

    List<PostDto> findAllPostsByCommunityId(Long CommunityId);

    List<PostDto> findHomePosts(Long userId);

    List<PostDto> findAllPostsByUserId(Long userId);

    List<PostDto> searchPosts(String query);
    

}
