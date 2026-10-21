package reddit.application.service;

import reddit.application.dto.VoteCommentDto;

public interface VoteCommentService {

    void createVoteComment(Long userId, Long commentId, VoteCommentDto voteCommentDto, int vote);

    VoteCommentDto findByUserIdAndCommentId(Long userId, Long commentId);

    void changeToUpvoteComment(Long userId, Long commentId, int vote);

    void changeToDownvoteComment(Long userId, Long commentId, int vote);

    void deleteUpvoteComment(Long userId, Long commentId);

    void deleteDownvoteComment(Long userId, Long commentId);

}
