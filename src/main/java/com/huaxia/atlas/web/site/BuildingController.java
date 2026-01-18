package com.huaxia.atlas.web.site;

import com.huaxia.atlas.ai.recommend.RelatedItemsService;
import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingInteractionService;
import com.huaxia.atlas.domain.building.BuildingService;
import com.huaxia.atlas.domain.comment.dto.CommentForm;
import com.huaxia.atlas.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/buildings")
public class BuildingController {

    private final BuildingService buildingService;
    private final RelatedItemsService relatedItemsService;
    private final BuildingInteractionService buildingInteractionService;
    private final UserService userService;

    public BuildingController(BuildingService buildingService,
                              RelatedItemsService relatedItemsService,
                              BuildingInteractionService buildingInteractionService,
                              UserService userService) {
        this.buildingService = buildingService;
        this.relatedItemsService = relatedItemsService;
        this.buildingInteractionService = buildingInteractionService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Building building = buildingService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var related = relatedItemsService.relatedTo(building, 6);

        model.addAttribute("building", building);
        model.addAttribute("related", related);
        model.addAttribute("comments", buildingInteractionService.listComments(id));
        model.addAttribute("likeCount", buildingInteractionService.countLikes(id));
        model.addAttribute("commentForm", new CommentForm());

        boolean liked = false;
        if (authentication != null && authentication.isAuthenticated()) {
            var userOpt = userService.findByLogin(authentication.getName());
            if (userOpt.isPresent()) {
                liked = buildingInteractionService.hasLiked(id, userOpt.get().getId());
            }
        }
        model.addAttribute("liked", liked);
        return "public/building-detail";
    }

    @PostMapping("/{id}/comment")
    public String addComment(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("commentForm") CommentForm form,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("error", "Comment cannot be empty.");
            return "redirect:/buildings/" + id;
        }

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        buildingInteractionService.addComment(id, user.getId(), form.getContent());
        ra.addFlashAttribute("success", "Comment added.");
        return "redirect:/buildings/" + id;
    }

    @PostMapping("/{id}/like")
    public String toggleLike(
            @PathVariable("id") Long id,
            Authentication authentication,
            RedirectAttributes ra) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        boolean liked = buildingInteractionService.toggleLike(id, user.getId());
        ra.addFlashAttribute("success", liked ? "Building liked." : "Like removed.");
        return "redirect:/buildings/" + id;
    }
}
