package reddit.application.controller;

import java.util.Comparator;
import java.util.List;

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
import reddit.application.dto.RegistrationDto;
import reddit.application.dto.UserEntityDto;
import reddit.application.models.UserEntity;
import reddit.application.security.SecurityUtil;
import reddit.application.service.CommentService;
import reddit.application.service.CommunityService;
import reddit.application.service.PostService;
import reddit.application.service.UserService;

@Controller
public class AuthController {
    private UserService userService;
    private CommunityService communityService;
    private PostService postService;
    private CommentService commentService;

    public AuthController(UserService userService, CommunityService communityService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.communityService = communityService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDto user, BindingResult result, Model model) {

        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if (existingUserEmail != null && existingUserEmail.getEmail() != null
                && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }

        UserEntity existingUserUsername = userService.findByUsername(user.getUsername());
        if (existingUserUsername != null && existingUserUsername.getUsername() != null
                && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/index?success";
    }

    @GetMapping("/profile/{vUserId}/posts")
    public String userDetail(@PathVariable("vUserId") Long vUserId, Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        if (user != null) {
            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);

            model.addAttribute("user", user);
        }
        

        UserEntity vUser = userService.findById(vUserId);
        model.addAttribute("vUser", vUser);       

        List<PostDto> posts = postService.findAllPostsByUserId(vUser.getId()).stream()
                .sorted(Comparator.comparing(PostDto::getCreatedOn).reversed()).toList();

        model.addAttribute("posts", posts);

        return "profile-detail-posts";
    }
    
    @GetMapping("/profile/{vUserId}/comments")
    public String userCommentsDetail(@PathVariable("vUserId") Long vUserId, Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);
        }
        

        UserEntity vUser = userService.findById(vUserId);
        model.addAttribute("vUser", vUser);
        

        List<CommentDto> comments = commentService.findAllCommentsByUserId(vUser.getId()).stream().sorted(Comparator.comparing(CommentDto::getCreatedOn).reversed()).toList();
        model.addAttribute("comments", comments);

        return "profile-detail-comments";
    }
    
    @GetMapping("/profile/{userId}/edit")
    public String editProfileForm(@PathVariable("userId") Long userId, Model model) {

        System.out.println("userID here is, " + userId);

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        model.addAttribute("user", user);

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());

        model.addAttribute("communities", communities);

        if (user.getId() != userId) {
            return "redirect:/profile/" + userId + "/posts";
        }

        UserEntityDto userEntityDto = new UserEntityDto();
        userEntityDto.setId(userId);
        userEntityDto.setEmail(user.getEmail());
        userEntityDto.setPhotoUrl(user.getPhotoUrl());
        model.addAttribute("userEntityDto", userEntityDto);


        return "profile-edit";

    }

    @PostMapping("/profile/{userId}/edit")
    public String updateProfile(@PathVariable("userId") Long userId,
            @Valid @ModelAttribute("userEntityDto") UserEntityDto userEntityDto, BindingResult result, Model model) {

        String username = SecurityUtil.getSessionUser();

        if (result.hasErrors()) {

            result.getAllErrors().forEach(error -> System.out.println(error));

            model.addAttribute("userEntityDto", userEntityDto);
            return "profile-edit";
        }

        userEntityDto.setId(userId);
        userService.updateUser(userEntityDto);

        return "redirect:/profile/" + userId + "/posts";

    }
    
    @GetMapping("/users/search")
    public String searchUsers(@RequestParam(value = "query") String search, Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);

        }
        

        List<UserEntity> users = userService.searchUsers(search);

        if (users.isEmpty()) {
            String emptyResults = "No results found in users";
            model.addAttribute("emptyResults", emptyResults);
        } else {
            model.addAttribute("users", users);
        }   

        model.addAttribute("search", search);
        return "profile-list";
    }

}
