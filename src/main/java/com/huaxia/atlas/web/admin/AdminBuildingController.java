package com.huaxia.atlas.web.admin;

import com.huaxia.atlas.domain.building.Building;
import com.huaxia.atlas.domain.building.BuildingService;
import com.huaxia.atlas.domain.building.dto.BuildingForm;
import com.huaxia.atlas.storage.ImageStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/buildings")
public class AdminBuildingController {

    private final BuildingService buildingService;
    private final ImageStorageService imageStorageService;

    public AdminBuildingController(BuildingService buildingService, ImageStorageService imageStorageService) {
        this.buildingService = buildingService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "dynasty", required = false) String dynasty,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model) {
        var result = buildingService.list(type, dynasty, page, size);

        model.addAttribute("type", type == null ? "" : type.trim());
        model.addAttribute("dynasty", dynasty == null ? "" : dynasty.trim());

        model.addAttribute("items", result.getContent());
        model.addAttribute("page", result);

        return "admin/buildings";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BuildingForm());
        model.addAttribute("mode", "create");
        return "admin/building-form";
    }

    @PostMapping("/new")
    public String create(
            @Valid @ModelAttribute("form") BuildingForm form,
            BindingResult bindingResult,
            @RequestParam(name = "coverFile", required = false) MultipartFile coverFile,
            RedirectAttributes ra,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/building-form";
        }

        try {
            String coverUrl = imageStorageService.saveCoverImage(coverFile);
            if (coverUrl != null)
                form.setCoverImage(coverUrl);
        } catch (Exception e) {
            bindingResult.reject("coverFile", e.getMessage());
            model.addAttribute("mode", "create");
            return "admin/building-form";
        }

        buildingService.create(form);
        ra.addFlashAttribute("success", "Building created.");
        return "redirect:/admin/buildings";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Building b = buildingService.get(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BuildingForm form = new BuildingForm();
        form.setName(b.getName());
        form.setDynasty(b.getDynasty());
        form.setLocation(b.getLocation());
        form.setType(b.getType());
        form.setYearBuilt(b.getYearBuilt());
        form.setDescription(b.getDescription());
        form.setTags(b.getTags());
        form.setCoverImage(b.getCoverImage());

        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("id", id);
        return "admin/building-form";
    }

    @PostMapping("/{id}/edit")
    public String edit(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("form") BuildingForm form,
            BindingResult bindingResult,
            @RequestParam(name = "coverFile", required = false) MultipartFile coverFile,
            RedirectAttributes ra,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("id", id);
            return "admin/building-form";
        }

        try {
            String coverUrl = imageStorageService.saveCoverImage(coverFile);
            if (coverUrl != null)
                form.setCoverImage(coverUrl);
        } catch (Exception e) {
            bindingResult.reject("coverFile", e.getMessage());
            model.addAttribute("mode", "edit");
            model.addAttribute("id", id);
            return "admin/building-form";
        }

        buildingService.update(id, form)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ra.addFlashAttribute("success", "Building updated.");
        return "redirect:/admin/buildings";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        buildingService.delete(id);
        ra.addFlashAttribute("success", "Building deleted.");
        return "redirect:/admin/buildings";
    }
}
