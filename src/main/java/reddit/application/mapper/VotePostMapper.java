package reddit.application.mapper;

import reddit.application.dto.VotePostDto;
import reddit.application.models.VotePost;

public class VotePostMapper {

    public static VotePost mapToVotePost(VotePostDto votePostDto) {
        return VotePost.builder()
                .id(votePostDto.getId())
                .user(votePostDto.getUser())
                .post(votePostDto.getPost())
                .vote(votePostDto.getVote())
                .build();
    }

    public static VotePostDto mapToVotePostDto(VotePost votePost) {
        return VotePostDto.builder()
                .id(votePost.getId())
                .user(votePost.getUser())
                .post(votePost.getPost())
                .vote(votePost.getVote())
                .build();

    }

}
