package reddit.application.service.impl;

import static reddit.application.mapper.CommentMapper.mapToComment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import reddit.application.dto.CommentDto;
import reddit.application.models.Comment;
import reddit.application.models.Post;
import reddit.application.models.UserEntity;
import reddit.application.repository.CommentRepository;
import reddit.application.repository.PostRepository;
import reddit.application.repository.UserRepository;
import reddit.application.service.CommentService;

import static reddit.application.mapper.CommentMapper.mapToCommentDto;
import static reddit.application.mapper.CommentMapper.mapToComment;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    


    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void createComment(Long postId, Long userId, Long parentCommentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).get();
        Comment parentComment;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId).get();
        } else {
            parentComment = null;
        }

        UserEntity user = userRepository.findById(userId).get();
        Comment comment = mapToComment(commentDto);
        comment.setPost(post);
        comment.setParentComment(parentComment);
        comment.setCreatedBy(user);
        comment.setVotes(0);
        commentRepository.save(comment);   
    }

    @Override
    public List<CommentDto> findAllComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndParentCommentIsNull(postId);
        return comments.stream().map(comment -> mapToCommentDto(comment)).collect(Collectors.toList());
        
    }

    @Override
    public CommentDto findByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        return mapToCommentDto(comment);
    }

    @Override
    public void updateComment(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).get();

        comment.setComment(commentDto.getComment());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


    @Override
    public List<CommentDto> findAllCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByCreatedById(userId);
        return comments.stream().map(comment -> mapToCommentDto(comment)).collect(Collectors.toList());
    }


    @Override
    public List<CommentDto> searchComments(String query) {
        List<Comment> comments = commentRepository.searchComment(query);
        return comments.stream().map(comment -> mapToCommentDto(comment)).collect(Collectors.toList());
    }

}
