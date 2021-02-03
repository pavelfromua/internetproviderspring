package my.project.internetprovider.controllers;

import my.project.internetprovider.models.Role;
import my.project.internetprovider.models.ProviderUser;
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
@RequestMapping("/users")
//@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String index(Model model) {
        model.addAttribute("users", userService.getUsers());

        return "users/list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));

        return "users/show";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") ProviderUser user) {
        return "/users/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid ProviderUser user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/new";
        }

        try {
            userService.addNewUser(user, Role.USER);
        } catch (IllegalStateException e) {
            FieldError fieldError = new FieldError("user","login","new.user.login.isTaken");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors())
            return "users/new";

        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, Principal principal) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("user_role", userService.getCurrentRole(principal.getName()));

        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid ProviderUser user,
                         BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "users/edit";

        userService.updateUser(user, id);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return "redirect:/users";
    }
}
