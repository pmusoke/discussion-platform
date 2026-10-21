package reddit.application.service;

import reddit.application.dto.VotePostDto;

public interface VotePostService {

    void createVotePost(Long userId, Long postId, VotePostDto votePostDto, int vote);

    VotePostDto findVoteByUserIdAndPostId(Long userId, Long postId);

    void changeToUpvotePost(Long userId, Long postId, int vote);

    void changeToDownvotePost(Long userId, Long postId, int vote);

    void deleteUpvotePost(Long userId, Long postId);

    void deleteDownvotePost(Long userId, Long postId);



}
