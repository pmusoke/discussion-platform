package reddit.application.service.impl;

import reddit.application.dto.VoteCommentDto;
import reddit.application.models.Comment;
import reddit.application.models.UserEntity;
import reddit.application.models.VoteComment;
import reddit.application.repository.CommentRepository;
import reddit.application.repository.UserRepository;
import reddit.application.repository.VoteCommentRepository;
import reddit.application.service.VoteCommentService;

import static reddit.application.mapper.VoteCommentMapper.mapToVoteComment;
import static reddit.application.mapper.VoteCommentMapper.mapToVoteCommentDto;

import org.springframework.stereotype.Service;

@Service
public class VoteCommentServiceImpl implements VoteCommentService {
    private VoteCommentRepository voteCommentRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository; 


    public VoteCommentServiceImpl(VoteCommentRepository voteCommentRepository, UserRepository userRepository,
            CommentRepository commentRepository) {
        this.voteCommentRepository = voteCommentRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    public void createVoteComment(Long userId, Long commentId, VoteCommentDto voteCommentDto, int vote) {
        UserEntity user = userRepository.findById(userId).get();
        Comment comment = commentRepository.findById(commentId).get();
        VoteComment voteComment = mapToVoteComment(voteCommentDto);

        voteComment.setUser(user);
        voteComment.setComment(comment);
        voteComment.setVote(vote);
        voteCommentRepository.save(voteComment);

        comment.setVotes(comment.getVotes() + vote);
        commentRepository.save(comment);
        
        

    }

    @Override
    public VoteCommentDto findByUserIdAndCommentId(Long userId, Long commentId) {
        VoteComment voteComment = voteCommentRepository.findByUserIdAndCommentId(userId, commentId);

        if (voteComment == null) {
            return null;
        }

        return mapToVoteCommentDto(voteComment);
    }

    @Override
    public void changeToUpvoteComment(Long userId, Long commentId, int vote) {
        Comment comment = commentRepository.findById(commentId).get();
        VoteComment voteComment = voteCommentRepository.findByUserIdAndCommentId(userId, commentId);
        voteComment.setVote(vote);

        comment.setVotes(comment.getVotes() + 2);
        commentRepository.save(comment);
        
    }

    @Override
    public void changeToDownvoteComment(Long userId, Long commentId, int vote) {
        Comment comment = commentRepository.findById(commentId).get();
        VoteComment voteComment = voteCommentRepository.findByUserIdAndCommentId(userId, commentId);
        voteComment.setVote(vote);

        comment.setVotes(comment.getVotes() - 2);
        commentRepository.save(comment);

    }

    @Override
    public void deleteUpvoteComment(Long userId, Long commentId) {
        VoteComment voteComment = voteCommentRepository.findByUserIdAndCommentId(userId, commentId);
        voteCommentRepository.deleteById(voteComment.getId());

        Comment comment = commentRepository.findById(commentId).get();
        comment.setVotes(comment.getVotes() - 1);
        commentRepository.save(comment);
    }

    @Override
    public void deleteDownvoteComment(Long userId, Long commentId) {
        VoteComment voteComment = voteCommentRepository.findByUserIdAndCommentId(userId, commentId);
        voteCommentRepository.deleteById(voteComment.getId());

        Comment comment = commentRepository.findById(commentId).get();
        comment.setVotes(comment.getVotes() + 1);
        commentRepository.save(comment);
    }

}
