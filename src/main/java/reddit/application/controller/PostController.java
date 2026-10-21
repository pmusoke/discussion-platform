package reddit.application.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import reddit.application.dto.CommentDto;
import reddit.application.dto.CommunityDto;
import reddit.application.dto.PostDto;
import reddit.application.dto.VoteCommentDto;
import reddit.application.dto.VotePostDto;
import reddit.application.models.Comment;
import reddit.application.models.Post;
import reddit.application.models.UserEntity;
import reddit.application.models.VotePost;
import reddit.application.security.SecurityUtil;
import reddit.application.service.CommentService;
import reddit.application.service.CommunityService;
import reddit.application.service.PostService;
import reddit.application.service.UserService;
import reddit.application.service.VoteCommentService;
import reddit.application.service.VotePostService;

@Controller
public class PostController {
    
    private PostService postService;
    private UserService userService;
    private CommunityService communityService;
    private CommentService commentService;
    private VotePostService votePostService;
    private VoteCommentService voteCommentService;



    @Autowired
    public PostController(PostService postService, UserService userService, CommunityService communityService, CommentService commentService, VotePostService votePostService, VoteCommentService voteCommentService) {
        this.postService = postService;
        this.userService = userService;
        this.communityService = communityService;
        this.commentService = commentService;
        this.votePostService = votePostService;
        this.voteCommentService = voteCommentService;
    }

    @GetMapping({ "/", "/index" })
    public String postList(Model model) {
        List<PostDto> posts = postService.findAllPosts();

        posts.sort(Comparator.comparing(PostDto::getCreatedOn).reversed());

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        System.out.println("user is " + user);

        if (user == null) {
            if (posts.isEmpty()) {
                String message = "Index page will be populated once posts have been made";
                model.addAttribute("message", message);
            } else {
                model.addAttribute("posts", posts);
            }
            
            return "index";
        }

        model.addAttribute("user", user);

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());

        model.addAttribute("communities", communities);

        if (posts.isEmpty()) {
            String message = "Index page will be populated once posts have been made";
            model.addAttribute("message", message);
            return "index";
        } 

        model.addAttribute("posts", posts);     

        VotePost upvote = new VotePost();
        model.addAttribute("upvotePost", upvote);

        VotePost downvote = new VotePost();
        model.addAttribute("downvotePost", downvote);

        return "index";
    }
    
    @GetMapping("/home")
    public String homePostList(Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        model.addAttribute("user", user);

        List<PostDto> posts = postService.findHomePosts(user.getId()).stream().sorted(Comparator.comparing(PostDto::getCreatedOn).reversed()).toList();

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());

        model.addAttribute("communities", communities);

        if (posts.isEmpty()) {
            String message = "Index page will be populated once posts have been made";
            model.addAttribute("message", message);
            return "index";
        }

        model.addAttribute("posts", posts);

        

        return "home";

    }

    @GetMapping("/posts/{postId}")
    public String viewPost(@PathVariable("postId") Long postId, Model model) {
        PostDto postDto = postService.findByPostId(postId);
        model.addAttribute("post", postDto);

        List<CommentDto> comments = commentService.findAllComments(postId);
        comments.sort(Comparator.comparing(CommentDto::getCreatedOn).reversed());
        sortReplies(comments);
        model.addAttribute("comments", comments);

        Comment comment = new Comment();
        model.addAttribute("comment", comment);

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);
            
            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);
        }
        
        

        return "post-detail";
    }

    private void sortReplies(List<CommentDto> comments) {

        
        for (CommentDto comment : comments) {

            if (comment.getReplies() != null) {
                comment.getReplies().sort(Comparator.comparing(CommentDto::getCreatedOn).reversed());

                sortReplies(comment.getReplies());
            }
        }
    }

    @GetMapping("/posts/{postId}/edit")
    public String editPostForm(@PathVariable("postId") Long postId, Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        model.addAttribute("user", user);

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());

        model.addAttribute("communities", communities);

        PostDto post = postService.findByPostId(postId);


        model.addAttribute("post", post);
        System.out.println("creator: " + post.getCreatedBy().getUsername() + " vistor: " + username);

        if (!post.getCreatedBy().getUsername().equals(username)) {
            System.out.println("This is triggered");

            return "redirect:/posts/" + postId;

        } else {
            return "post-edit";
        }


    }

    @PostMapping("/posts/{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId, @Valid @ModelAttribute("post") PostDto post, BindingResult result, Model model) {
        
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        model.addAttribute("user", user);
        
        if (result.hasErrors()) {
            model.addAttribute("post", post);
            return "post-edit";
        }

        post.setId(postId);
        postService.updatePost(post);
        return "redirect:/posts/" + postId;

    }

    @GetMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId) {

        postService.deletePost(postId);
        return "redirect:/index";
    }

    @PostMapping("/posts/{postId}")
    public String createComment(@Valid @ModelAttribute("comment") CommentDto commentDto, BindingResult result ,@PathVariable("postId") Long postId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("comment", commentDto);

            String username = SecurityUtil.getSessionUser();
            UserEntity user = userService.findByUsername(username);
            model.addAttribute("user", user);

            PostDto postDto = postService.findByPostId(postId);
            model.addAttribute("post", postDto);

            List<CommentDto> comments = commentService.findAllComments(postId);
            model.addAttribute("comments", comments);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);


            return "post-detail";
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        commentService.createComment(postId, user.getId(), null, commentDto);
        return "redirect:/posts/{postId}";
    }


    @PostMapping("posts/{postId}/{parentId}")
    public String createReply(@Valid @ModelAttribute("comment") CommentDto commentDto, BindingResult result, @PathVariable("postId") Long postId,
            @PathVariable("parentId") Long parentId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("comment", commentDto);

            String username = SecurityUtil.getSessionUser();
            UserEntity user = userService.findByUsername(username);
            model.addAttribute("user", user);

            PostDto postDto = postService.findByPostId(postId);
            model.addAttribute("post", postDto);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);

            List<CommentDto> comments = commentService.findAllComments(postId);
            model.addAttribute("comments", comments);

            return "post-detail";
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        commentService.createComment(postId, user.getId(), parentId, commentDto);
        return "redirect:/posts/{postId}";
    }

    /* @GetMapping("/posts/{communityId}/new")
    public String createPostForm(@PathVariable("communityId") Long communityId, Model model) {
        Post post = new Post();
        model.addAttribute("communityId", communityId);
        model.addAttribute("post", post);
        return "post-create";
    } */

    @GetMapping("/comments/{commentId}/edit")
    public String editCommentForm(@PathVariable("commentId") Long commentId, Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        model.addAttribute("user", user);

        CommentDto comment = commentService.findByCommentId(commentId);
        model.addAttribute("comment", comment);

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());

        model.addAttribute("communities", communities);

        

        return "comment-edit";
    }

    @PostMapping("/comments/{commentId}/edit")
    public String updateComment(@PathVariable("commentId") Long commentId, @Valid @ModelAttribute("comment") CommentDto comment, BindingResult result, Model model) {
        
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        model.addAttribute("user", user);

        if (result.hasErrors()) {
            model.addAttribute("comment", comment);
            return "comment-edit";
        }
        
        commentService.updateComment(commentId, comment);

        Long postId = commentService.findByCommentId(commentId).getPost().getId();

        return "redirect:/posts/" + postId;

    }

    @GetMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Long commentId) {

        Long postId = commentService.findByCommentId(commentId).getPost().getId();

        commentService.deleteComment(commentId);

        return "redirect:/posts/" + postId;

    }

    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        Post post = new Post();
        List<CommunityDto> communitys = communityService.findAllCommunities();
        model.addAttribute("communitys", communitys);
        model.addAttribute("post", post);

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
        model.addAttribute("communities", communities);

        model.addAttribute("user", user);

        return "post-create";

    }

    @PostMapping("/posts/new")
    public String createPost(@ModelAttribute("post") PostDto postDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("post", postDto);
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        postService.createPost(postDto.getCommunity().getId(), user.getId(), postDto);
        return "redirect:/index";
    }
    






    @PostMapping("/posts/{postId}/upvote")
    public String upvotePost(@PathVariable("postId") Long postId, @Valid @ModelAttribute("upvote") VotePostDto upvote,
            BindingResult result, Model model, @RequestParam String returnUrl) {
        
        if (result.hasErrors()) {
            model.addAttribute("upvote", upvote);
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        VotePostDto existingVote = votePostService.findVoteByUserIdAndPostId(user.getId(), postId);
        

        if (existingVote == null) {
            votePostService.createVotePost(user.getId(), postId, upvote, 1);
        } else if (existingVote.getVote() == 1) {
            votePostService.deleteUpvotePost(user.getId(), postId);           
        } else if (existingVote.getVote() == -1) {
            votePostService.changeToUpvotePost(user.getId(), postId, 1);
        }
        
        
        return "redirect:" + returnUrl;
    }

    @PostMapping("/posts/{postId}/downvote")
    public String downvotePost(@PathVariable("postId") Long postId,
            @Valid @ModelAttribute("downvote") VotePostDto downvote, BindingResult result, Model model,
            @RequestParam String returnUrl) {

        if (result.hasErrors()) {
            model.addAttribute("downvote", downvote);
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        VotePostDto existingVote = votePostService.findVoteByUserIdAndPostId(user.getId(), postId);

        if (existingVote == null) {
            votePostService.createVotePost(user.getId(), postId, downvote, -1);
        } else if (existingVote.getVote() == 1) {
            votePostService.changeToDownvotePost(user.getId(), postId, -1);
        } else if (existingVote.getVote() == -1) {
            votePostService.deleteDownvotePost(user.getId(), postId);
        }

        return "redirect:" + returnUrl;
    }

    @PostMapping("/comment/{commentId}/upvote")
    public String upvoteComment(@PathVariable("commentId") Long commentId,
            @Valid @ModelAttribute("upvoteCommentObject") VoteCommentDto upvoteCommentObject, BindingResult result, Model model,
            @RequestParam String returnUrl) {

        if (result.hasErrors()) {
            model.addAttribute("upvoteCommentObject", upvoteCommentObject);
        }

        System.out.println("This post mapping was triggered");

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        VoteCommentDto existingVote = voteCommentService.findByUserIdAndCommentId(user.getId(), commentId);

        if (existingVote == null) {
            voteCommentService.createVoteComment(user.getId(), commentId, upvoteCommentObject, 1);
            System.out.println("Vote was created");
        } else if (existingVote.getVote() == 1) {
            voteCommentService.deleteUpvoteComment(user.getId(), commentId);
        } else if (existingVote.getVote() == -1) {
            voteCommentService.changeToUpvoteComment(user.getId(), commentId, 1);
        }

        return "redirect:" + returnUrl;

    }
    
    @PostMapping("/comment/{commentId}/downvote")
    public String downvoteComment(@PathVariable("commentId") Long commentId,
            @Valid @ModelAttribute("downvoteCommentObject") VoteCommentDto downvoteCommentObject, BindingResult result,
            Model model, @RequestParam String returnUrl) {

        if (result.hasErrors()) {
            model.addAttribute("downvoteCommentObject", downvoteCommentObject);
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        VoteCommentDto existingVote = voteCommentService.findByUserIdAndCommentId(user.getId(), commentId);

        if (existingVote == null) {
            voteCommentService.createVoteComment(user.getId(), commentId, downvoteCommentObject, -1);
        } else if (existingVote.getVote() == 1) {
            voteCommentService.changeToDownvoteComment(user.getId(), commentId, -1);
        } else if (existingVote.getVote() == -1) {
            voteCommentService.deleteDownvoteComment(user.getId(), commentId);
        }

        return "redirect:" + returnUrl;
    }
    
    @GetMapping("/posts/search")
    public String searchPosts(@RequestParam(value = "query") String query, Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);
        }   

        

        List<PostDto> posts = postService.searchPosts(query);

        if (posts.isEmpty()) {
            String emptyResults = "No results found in posts";
            model.addAttribute("emptyResults", emptyResults);
        } else {
            model.addAttribute("posts", posts);
        }

        model.addAttribute("search", query);
        return "post-list";
    }

    @GetMapping("/comments/search")
    public String searchComments(@RequestParam(value = "query") String search, Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);
        }
        

        List<CommentDto> comments = commentService.searchComments(search);

        if (comments.isEmpty()) {
            String emptyResults = "No results found in comments";
            model.addAttribute("emptyResults", emptyResults);
        } else {
            model.addAttribute("comments", comments);
        }
        

        model.addAttribute("search", search);
        return "comment-list";

    }
    

    

}
