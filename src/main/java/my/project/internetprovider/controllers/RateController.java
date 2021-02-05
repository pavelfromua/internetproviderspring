package my.project.internetprovider.controllers;

import my.project.internetprovider.models.ProviderService;
import my.project.internetprovider.models.ProviderUser;
import my.project.internetprovider.models.Rate;
import my.project.internetprovider.models.Role;
import my.project.internetprovider.services.ProviderServiceService;
import my.project.internetprovider.services.RateService;
import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
@RequestMapping("/products")
public class RateController {
    private final ProviderServiceService providerServiceService;
    private final UserService userService;
    private final RateService rateService;

    @Autowired
    public RateController(ProviderServiceService providerServiceService,
                          UserService userService,
                          RateService rateService) {
        this.providerServiceService = providerServiceService;
        this.userService = userService;
        this.rateService = rateService;
    }

    @GetMapping("/rates")
    public String index(Model model) {
        model.addAttribute("serviceList", providerServiceService.getServices());
        model.addAttribute("rates", rateService.getRates());

        return "products/rates/list";
    }

    @GetMapping("/rates/sid/{sid}")
    public String filterByServiceId(Model model, @PathVariable("sid") Long serviceId) {
        model.addAttribute("serviceList", providerServiceService.getServices());
        model.addAttribute("rates", rateService.getRatesByServiceId(serviceId));

        return "products/rates/list";
    }

    @GetMapping("/rates/new")
    public String newRate(Model model, @ModelAttribute("rate") Rate rate) {
        model.addAttribute("serviceList", providerServiceService.getServices());

        return "/products/rates/new";
    }

    @PostMapping("/rates")
    public String createRate(Model model, @ModelAttribute("rate") @Valid Rate rate, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("serviceList", providerServiceService.getServices());

            return "products/rates/new";
        }

        rateService.addNewRate(rate);

        return "redirect:/products/rates";
    }

    @GetMapping("/rates/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(Model model, @PathVariable("id") Long id, Principal principal) {
        model.addAttribute("serviceList", providerServiceService.getServices());
        model.addAttribute("rate", rateService.getRateById(id));
        model.addAttribute("user_role", userService.getCurrentRole(principal.getName()));

        return "products/rates/edit";
    }

    @PatchMapping("/rates/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@ModelAttribute("rate") @Valid Rate rate,
                         BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "products/rates/edit";

        rateService.updateRate(rate, id);
        return "redirect:/products/rates";
    }

    @DeleteMapping("/rates/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        rateService.deleteRate(id);

        return "redirect:/products/rates";
    }
}
