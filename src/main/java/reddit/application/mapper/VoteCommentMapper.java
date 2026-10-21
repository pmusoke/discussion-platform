package reddit.application.mapper;

import reddit.application.dto.VoteCommentDto;
import reddit.application.models.VoteComment;


public class VoteCommentMapper {

    public static VoteComment mapToVoteComment(VoteCommentDto voteCommentDto) {
        return VoteComment.builder()
                .id(voteCommentDto.getId())
                .user(voteCommentDto.getUser())
                .comment(voteCommentDto.getComment())
                .vote(voteCommentDto.getVote())
                .build();
    }

    public static VoteCommentDto mapToVoteCommentDto(VoteComment voteComment) {
        return VoteCommentDto.builder()
                .id(voteComment.getId())
                .user(voteComment.getUser())
                .comment(voteComment.getComment())
                .vote(voteComment.getVote())
                .build();

    }

}
