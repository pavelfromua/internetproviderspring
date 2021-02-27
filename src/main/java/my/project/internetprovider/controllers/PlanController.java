package my.project.internetprovider.controllers;

import com.lowagie.text.DocumentException;
import my.project.internetprovider.exception.DBException;
import my.project.internetprovider.models.Plan;
import my.project.internetprovider.models.User;
import my.project.internetprovider.services.PlanService;
import my.project.internetprovider.services.UserService;
import my.project.internetprovider.utils.PlanPDFExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        return listByPage(model, 1, "price", "asc", authUser);
    }

    @GetMapping("/plans/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @Param("sf") String sf,
                             @Param("sd") String sd,
                             @AuthenticationPrincipal User authUser) {
        Page<Plan> page = planService.getPlans(currentPage, sf, sd);

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Plan> planList = page.getContent();
        String rsd = "asc".equals(sd) ? "desc" : "asc";

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("sf", sf);
        model.addAttribute("sd", sd);
        model.addAttribute("rsd", rsd);
        model.addAttribute("plans", planList);
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

        try {
            planService.savePlan(plan);
        } catch (DBException e) {}

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
    public String delete(Model model, @PathVariable("id") Long id) {
        try {
            planService.deletePlan(id);
        } catch (DBException e) {
        }

        return "redirect:/items/plans";
    }

    @GetMapping("/plans/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=plans_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Plan> listPlans = planService.getPlans();

        PlanPDFExporter exporter = new PlanPDFExporter(listPlans);
        exporter.export(response);
    }
}
