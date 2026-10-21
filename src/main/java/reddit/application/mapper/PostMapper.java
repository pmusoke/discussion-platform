package reddit.application.mapper;

import java.util.stream.Collectors;

import reddit.application.dto.PostDto;
import reddit.application.models.Post;

import static reddit.application.mapper.CommentMapper.mapToCommentDto;
import static reddit.application.mapper.VotePostMapper.mapToVotePostDto;

public class PostMapper {

    public static Post mapToPost(PostDto postDto) {
        return Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .photoUrl(postDto.getPhotoUrl())
                .createdOn(postDto.getCreatedOn())
                .updatedOn(postDto.getUpdatedOn())
                .community(postDto.getCommunity())
                .createdBy(postDto.getCreatedBy())
                .build();
    }

    public static PostDto mapToPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .photoUrl(post.getPhotoUrl())
                .votes(post.getVotes())
                .createdOn(post.getCreatedOn())
                .updatedOn(post.getUpdatedOn())
                .community(post.getCommunity())
                .createdBy(post.getCreatedBy())
                .comments(post.getComments().stream().map((comment) -> mapToCommentDto(comment)).collect(Collectors.toList()))
                .votePosts(post.getVotePosts().stream().map((votePost) -> mapToVotePostDto(votePost)).collect(Collectors.toList()))
                .build();
    }



}
