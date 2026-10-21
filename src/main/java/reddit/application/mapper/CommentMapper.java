package reddit.application.mapper;

import static reddit.application.mapper.VoteCommentMapper.mapToVoteCommentDto;

import java.util.stream.Collectors;

import reddit.application.dto.CommentDto;
import reddit.application.models.Comment;

public class CommentMapper {

    public static Comment mapToComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .comment(commentDto.getComment())
                .votes(commentDto.getVotes())
                .createdOn(commentDto.getCreatedOn())
                .updatedOn(commentDto.getUpdatedOn())
                .post(commentDto.getPost())
                .parentComment(commentDto.getParentComment())
                .createdBy(commentDto.getCreatedBy())
                .build();
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .votes(comment.getVotes())
                .createdOn(comment.getCreatedOn())
                .updatedOn(comment.getUpdatedOn())
                .post(comment.getPost())
                .parentComment(comment.getParentComment())
                .replies(comment.getReplies().stream().map((reply) -> mapToCommentDto(reply)).collect(Collectors.toList()))
                .createdBy(comment.getCreatedBy())
                .voteComments(comment.getVoteComments().stream().map((voteComment) -> mapToVoteCommentDto(voteComment)).collect(Collectors.toList()))
                .build();

    }

}
