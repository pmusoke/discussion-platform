package reddit.application.service.impl;

import org.springframework.stereotype.Service;

import reddit.application.dto.VotePostDto;
import reddit.application.models.Post;
import reddit.application.models.UserEntity;
import reddit.application.models.VotePost;
import reddit.application.repository.PostRepository;
import reddit.application.repository.UserRepository;
import reddit.application.repository.VotePostRepository;
import reddit.application.service.VotePostService;

import static reddit.application.mapper.VotePostMapper.mapToVotePost;
import static reddit.application.mapper.VotePostMapper.mapToVotePostDto;

@Service
public class VotePostServiceImpl implements VotePostService {
    private VotePostRepository votePostRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    

    public VotePostServiceImpl(VotePostRepository votePostRepository, UserRepository userRepository,
            PostRepository postRepository) {
        this.votePostRepository = votePostRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    @Override
    public void createVotePost(Long userId, Long postId, VotePostDto votePostDto, int vote) {
        UserEntity user = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        VotePost votePost = mapToVotePost(votePostDto);

        votePost.setPost(post);
        votePost.setUser(user);
        votePost.setVote(vote);
        votePostRepository.save(votePost);

        post.setVotes(post.getVotes() + vote);
        postRepository.save(post);
    }


    @Override
    public VotePostDto findVoteByUserIdAndPostId(Long userId, Long postId) {
        VotePost votePost = votePostRepository.findByUserIdAndPostId(userId, postId);

        if (votePost == null) {
            return null;
        }

        return mapToVotePostDto(votePost);
    }


    @Override
    public void changeToUpvotePost(Long userId, Long postId, int vote) {
        Post post = postRepository.findById(postId).get();
        VotePost votePost = votePostRepository.findByUserIdAndPostId(userId, postId);
        votePost.setVote(vote);
        votePostRepository.save(votePost);

        post.setVotes(post.getVotes() + 2);
        postRepository.save(post);
    }


    @Override
    public void deleteUpvotePost(Long userId, Long postId) {
        VotePost votePost = votePostRepository.findByUserIdAndPostId(userId, postId);
        votePostRepository.deleteById(votePost.getId());

        Post post = postRepository.findById(postId).get();
        post.setVotes(post.getVotes() - 1);
        postRepository.save(post);
    }


    @Override
    public void changeToDownvotePost(Long userId, Long postId, int vote) {
        Post post = postRepository.findById(postId).get();
        VotePost votePost = votePostRepository.findByUserIdAndPostId(userId, postId);
        votePost.setVote(vote);
        votePostRepository.save(votePost);

        post.setVotes(post.getVotes() - 2);
        postRepository.save(post);

    }


    @Override
    public void deleteDownvotePost(Long userId, Long postId) {
        VotePost votePost = votePostRepository.findByUserIdAndPostId(userId, postId);
        votePostRepository.delete(votePost);

        Post post = postRepository.findById(postId).get();
        post.setVotes(post.getVotes() + 1);
        postRepository.save(post);
    }


    

}
