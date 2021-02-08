package my.project.internetprovider.controllers;

import my.project.internetprovider.models.User;
import my.project.internetprovider.services.AccountService;
import my.project.internetprovider.services.PlanService;
import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/acc")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public AccountController(AccountService accountService,
                             UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/plans/{uid}/{aid}/{pid}/{plid}")
    public String addPlanToAccount(Model model,
                                   @PathVariable("uid") Long userId,
                                   @PathVariable("aid") Long accountId,
                                   @PathVariable("pid") Long productId,
                                   @ModelAttribute("plid") Long planId) {
        model.addAttribute("plans", accountService.getPlansByProductId(productId));
        model.addAttribute("uid", userId);
        model.addAttribute("aid", accountId);
        model.addAttribute("pid", productId);
        model.addAttribute("plid", planId);

        return "items/plans/assign";
    }

    @PostMapping("/plan/assign")
    public String create(@ModelAttribute("plan") Long planId,
                         @ModelAttribute("btnValue") String btnValue,
                         @ModelAttribute("uid") Long userId,
                         @ModelAttribute("aid") Long accountId,
                         @ModelAttribute("pid") Long productId,

                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/users/" + String.format("/acc/plans/%s/%s/%s/%s", userId, accountId, productId, planId);
        }

        if ("assign".equals(btnValue))
            accountService.assignPlanToAccount(accountId, planId);

        return "redirect:/users/" + userId;
    }

    @PostMapping("/pay")
    public String savePayment(@ModelAttribute("uid") Long userId,
                              @ModelAttribute("aid") Long accountId,
                              @ModelAttribute("amount") Double amount,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/users/" + userId;
        }

        accountService.savePayment(accountId, amount);

        return "redirect:/users/" + userId;
    }

    @PostMapping("/status")
    public String savePayment(@ModelAttribute("uid") Long userId,
                              @ModelAttribute("aid") Long accountId,
                              @ModelAttribute("as") Boolean status,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/users/" + userId;
        }

        accountService.setAccountStatus(accountId, status);

        return "redirect:/users/" + userId;
    }
}
