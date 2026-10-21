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
import reddit.application.dto.CommunityDto;
import reddit.application.dto.MembershipDto;
import reddit.application.dto.PostDto;
import reddit.application.models.Community;
import reddit.application.models.Membership;
import reddit.application.models.UserEntity;
import reddit.application.security.SecurityUtil;
import reddit.application.service.CommunityService;
import reddit.application.service.MembershipService;
import reddit.application.service.PostService;
import reddit.application.service.UserService;

@Controller
public class CommunityController {
    private CommunityService communityService;
    private UserService userService;
    private PostService postService;
    private MembershipService membershipService;

    @Autowired
    public CommunityController(CommunityService communityService, UserService userService, PostService postService, MembershipService membershipService) {
        this.communityService = communityService;
        this.userService = userService;
        this.postService = postService;
        this.membershipService = membershipService;
    }

    @GetMapping("/communities")
    public String listCommunities(Model model) {
        UserEntity user = new UserEntity();
        List<CommunityDto> communities = communityService.findAllCommunities();
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        

        model.addAttribute("user", user);
        model.addAttribute("communities", communities);

        return "community-list";

        
    }

    @GetMapping("/communities/new")
    public String createCommunityForm(Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        model.addAttribute("user", user);



        Community community = new Community();
        model.addAttribute("community", community);

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());

        model.addAttribute("communities", communities);
        return "communities-create";
    }

    @PostMapping("/communities/new")
    public String saveCommunity(@Valid @ModelAttribute("community") CommunityDto communityDto, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("community", communityDto);

            String username = SecurityUtil.getSessionUser();
            UserEntity user = userService.findByUsername(username);
            model.addAttribute("user", user);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);

            return "communities-create";
        }
        
        communityService.saveCommunity(communityDto);
        return "redirect:/index";
        
                
    }

    @GetMapping("/community/{communityName}/edit")
    public String editCommunityForm(@PathVariable("communityName") String communityName, Model model) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        model.addAttribute("user", user);

        CommunityDto community = communityService.findCommunityByName(communityName);
        model.addAttribute("community", community);

        List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
        model.addAttribute("communities", communities);

        return "communities-edit";
    }

    @PostMapping("/community/{communityName}/edit")
    public String updateCommunity(@PathVariable("communityName") String communityName, @Valid @ModelAttribute("community") CommunityDto community, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("community", community);
            return "communities-edit";
        }
        System.out.println("community is " + community);

        communityService.updateCommunity(communityName, community);
        return "redirect:/community/" + community.getName();
    }



    @GetMapping("/community/{communityName}")
    public String viewCommunity(@PathVariable("communityName") String communityName, Model model) {

        CommunityDto communityDto = communityService.findCommunityByName(communityName);
        model.addAttribute("community", communityDto);

        List<PostDto> postDtos = postService.findAllPostsByCommunityId(communityDto.getId()).stream().sorted(Comparator.comparing(PostDto::getCreatedOn).reversed()).toList();
        model.addAttribute("posts", postDtos);

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        model.addAttribute("user", user);

        if (user != null) {
            MembershipDto membershipDto = membershipService.findByUserIdAndCommunityId(user.getId(), communityDto.getId());
            model.addAttribute("membershipDto", membershipDto);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);
        }
        

        Membership membership = new Membership();
        model.addAttribute("membership", membership);

        

        List<MembershipDto> memberships = membershipService.findAllByCommunityId(communityDto.getId());
        int membershipCount = memberships.size();
        model.addAttribute("membershipCount", membershipCount);



        return "communities-detail";

    }

    @GetMapping("/communities/{communityName}/delete")
    public String deleteCommunity(@PathVariable("communityName") String communityName) {

        communityService.delete(communityName);
        return "redirect:/index";
    }

    @PostMapping("/communities/join/{communityId}")
    public String joinCommunity(@Valid @ModelAttribute("membership") MembershipDto membershipDto, BindingResult result,
            Model model, @PathVariable("communityId") Long communityId) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        if (result.hasErrors()) {
            model.addAttribute("membership", membershipDto);
            return "communities-detail";
        }

        CommunityDto communityDto = communityService.findCommunityById(communityId);

        membershipService.createMembership(user.getId(), communityId, membershipDto);
        return "redirect:/community/" + communityDto.getName();

    }
    
    @PostMapping("/communities/unjoin/{communityId}")
    public String unjoinCommunity(@Valid @ModelAttribute("membership") MembershipDto membershipDto,
            BindingResult result, Model model, @PathVariable("communityId") Long communityId) {
        if (result.hasErrors()) {
            model.addAttribute("membership", membershipDto);
            return "communities-detail";
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        CommunityDto communityDto = communityService.findCommunityById(communityId);

        membershipService.deleteMembership(user.getId(), communityId);
        return "redirect:/community/" + communityDto.getName();

    }
    
    @GetMapping ("/communities/search")
    public String searchCommunities(@RequestParam("query") String search, Model model) {
        
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);

            List<CommunityDto> communities = communityService.findAllCommunitiesByUserId(user.getId());
            model.addAttribute("communities", communities);

        }

        List<CommunityDto> communitys = communityService.searchCommunities(search);

        if (communitys.isEmpty()) {
            String emptyResults = "No results found in communities";
            model.addAttribute("emptyResults", emptyResults);
        } else {
            model.addAttribute("communitys", communitys);
        }     

        model.addAttribute("search", search);
        return "communities-list";
    }

}
