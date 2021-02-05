package my.project.internetprovider.controllers;

import my.project.internetprovider.models.ProviderService;
import my.project.internetprovider.models.ProviderUser;
import my.project.internetprovider.models.Rate;
import my.project.internetprovider.models.Role;
import my.project.internetprovider.services.ProviderServiceService;
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

@Controller
@RequestMapping("/products")
public class ProviderServiceController {
    private final ProviderServiceService providerServiceService;
    private final UserService userService;

    @Autowired
    public ProviderServiceController(ProviderServiceService providerServiceService,
                                     UserService userService) {
        this.providerServiceService = providerServiceService;
        this.userService = userService;
    }

    @GetMapping("/services")
    public String index(Model model) {
        model.addAttribute("services", providerServiceService.getServices());

        return "products/services/list";
    }

    @GetMapping()
    public String showProducts(Model model) {
        return "redirect:/products/services";
    }

    @GetMapping("/services/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String newService(@ModelAttribute("service") ProviderService service) {
        return "/products/services/new";
    }

    @PostMapping("/services")
    public String createService(@ModelAttribute("service") @Valid ProviderService service, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "products/services/new";
        }

        providerServiceService.addNewService(service);

        return "redirect:/products/services";
    }

    @GetMapping("/services/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, Principal principal) {
        model.addAttribute("service", providerServiceService.getServiceById(id));
        model.addAttribute("user_role", userService.getCurrentRole(principal.getName()));

        return "products/services/edit";
    }

    @PatchMapping("/services/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@ModelAttribute("service") @Valid ProviderService service,
                         BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "products/services/edit";

        providerServiceService.updateService(service, id);
        return "redirect:/products/services";
    }

    @DeleteMapping("/services/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        providerServiceService.deleteService(id);

        return "redirect:/products/services";
    }
}
