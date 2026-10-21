package reddit.application.service;

import java.util.List;

import reddit.application.dto.CommentDto;

public interface CommentService {
    void createComment(Long postId, Long userId, Long parentCommentId, CommentDto commentDto);

    List<CommentDto> findAllComments(Long postId);

    List<CommentDto> findAllCommentsByUserId(Long userId);

    CommentDto findByCommentId(Long commentId);

    void updateComment(Long commentId, CommentDto commentDto);

    void deleteComment(Long commentId);

    List<CommentDto> searchComments(String query);
}
