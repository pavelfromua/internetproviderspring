package my.project.internetprovider.controllers;

import my.project.internetprovider.models.Plan;
import my.project.internetprovider.models.Product;
import my.project.internetprovider.models.User;
import my.project.internetprovider.services.ProductService;
import my.project.internetprovider.services.PlanService;
import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/items")
public class PlanController {
    private final PlanService planService;
    private final UserService userService;

    @Autowired
    public PlanController(PlanService planService, UserService userService) {
        this.planService = planService;
        this.userService = userService;
    }

    @GetMapping("/plans")
    public String index(Model model, @AuthenticationPrincipal User authUser) {
        model.addAllAttributes(planService.getDataForListOfPlans());
        model.addAttribute("user_role", userService.getRoleForUser(authUser));

        return "items/plans/list";
    }

    @GetMapping("/plans/pid/{pid}")
    public String filterBypPoductId(Model model, @PathVariable("pid") Long productId, @AuthenticationPrincipal User authUser) {
        model.addAllAttributes(planService.getDataForListOfPlansByProductId(productId));
        model.addAttribute("user_role", userService.getRoleForUser(authUser));

        return "items/plans/list";
    }

    @GetMapping("/plans/new")
    public String newPlan(Model model, @ModelAttribute("plan") Plan plan) {
        model.addAttribute("products", planService.getProducts());

        return "/items/plans/new";
    }

    @PostMapping("/plans")
    public String createPlan(Model model, @ModelAttribute("plan") @Valid Plan plan, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("products", planService.getProducts());

            return "items/plans/new";
        }

        planService.savePlan(plan);

        return "redirect:/items/plans";
    }

    @GetMapping("/plans/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAllAttributes(planService.getDataForEditPlan(id));

        return "items/plans/edit";
    }

    @PatchMapping("/plans/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@ModelAttribute("plan") @Valid Plan plan, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "items/plans/edit";

        planService.updatePlan(plan);

        return "redirect:/items/plans";
    }

    @DeleteMapping("/plans/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        planService.deletePlan(id);

        return "redirect:/items/plans";
    }
}
