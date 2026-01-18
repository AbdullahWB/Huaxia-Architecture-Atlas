package com.huaxia.atlas.web.site;

import com.huaxia.atlas.domain.building.BuildingLikeRepository;
import com.huaxia.atlas.domain.message.MessageService;
import com.huaxia.atlas.domain.notification.UserNotificationService;
import com.huaxia.atlas.domain.order.OrderRepository;
import com.huaxia.atlas.domain.post.PostLikeRepository;
import com.huaxia.atlas.domain.post.PostRepository;
import com.huaxia.atlas.domain.user.UserService;
import com.huaxia.atlas.domain.user.dto.UserProfileForm;
import com.huaxia.atlas.storage.ImageStorageService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController {

    private final UserService userService;
    private final PostRepository postRepository;
    private final OrderRepository orderRepository;
    private final PostLikeRepository postLikeRepository;
    private final BuildingLikeRepository buildingLikeRepository;
    private final UserNotificationService notificationService;
    private final MessageService messageService;
    private final ImageStorageService imageStorageService;

    public UserDashboardController(UserService userService,
                                   PostRepository postRepository,
                                   OrderRepository orderRepository,
                                   PostLikeRepository postLikeRepository,
                                   BuildingLikeRepository buildingLikeRepository,
                                   UserNotificationService notificationService,
                                   MessageService messageService,
                                   ImageStorageService imageStorageService) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.orderRepository = orderRepository;
        this.postLikeRepository = postLikeRepository;
        this.buildingLikeRepository = buildingLikeRepository;
        this.notificationService = notificationService;
        this.messageService = messageService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        long postCount = postRepository.countByAuthorEmailIgnoreCase(user.getEmail());
        long orderCount = orderRepository.countByUserId(user.getId());
        long postLikes = postLikeRepository.countByUserId(user.getId());
        long buildingLikes = buildingLikeRepository.countByUserId(user.getId());

        UserProfileForm profileForm = new UserProfileForm();
        profileForm.setBio(user.getBio());

        model.addAttribute("user", user);
        model.addAttribute("postCount", postCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("postLikeCount", postLikes);
        model.addAttribute("buildingLikeCount", buildingLikes);
        model.addAttribute("profileForm", profileForm);
        model.addAttribute("notifications", notificationService.latestForUser(user.getId()));
        model.addAttribute("unreadNotificationCount", notificationService.countUnread(user.getId()));
        model.addAttribute("adminMessages", messageService.listForEmail(user.getEmail()));
        return "public/user-dashboard";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @Valid UserProfileForm profileForm,
            BindingResult bindingResult,
            @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile,
            Authentication authentication,
            RedirectAttributes ra) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("error", "Profile description is too long.");
            return "redirect:/dashboard";
        }

        var user = userService.findByLogin(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String avatarUrl = null;
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                avatarUrl = imageStorageService.saveAvatarImage(avatarFile);
            } catch (Exception ex) {
                ra.addFlashAttribute("error", ex.getMessage());
                return "redirect:/dashboard";
            }
        }

        userService.updateProfile(user.getId(), profileForm.getBio(), avatarUrl);
        ra.addFlashAttribute("success", "Profile updated.");
        return "redirect:/dashboard";
    }
}
