package my.project.internetprovider.controllers;

import my.project.internetprovider.models.User;
import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

@Controller
@RequestMapping("/users")
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
    public String showCabinet(Model model, @PathVariable("id") Long id,
                              @AuthenticationPrincipal User authUser) {
        String userRole = userService.getRoleForUser(authUser);

        if (!"ROLE_ADMIN".equals(userRole) && id != authUser.getId())
            return "errors/403";

        model.addAllAttributes(userService.getDataForUserCabinet(id));
        model.addAttribute("user_role", userRole);

        return "users/show";
    }

    @GetMapping("/cabinet")
    public String goToCabinet(@AuthenticationPrincipal User authUser) {
        if (!"ROLE_ADMIN".equals(userService.getRoleForUser(authUser)))
            return "redirect:/users/" + authUser.getId();

        return "users/list";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());

        return "/users/new";
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/new";
        }

        if (!userService.saveUser(user)) {
            FieldError fieldError = new FieldError("user","username","new.user.login.isTaken");
            bindingResult.addError(fieldError);

            return "users/new";
        }

        return "redirect:/users";
    }

    @PostMapping("/edit")
    public String edit(Model model, @ModelAttribute("user") @Valid User user,
                       @AuthenticationPrincipal User authUser) {
        model.addAttribute("user", user);
        model.addAttribute("user_role", userService.getRoleForUser(authUser));

        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/edit";

        userService.updateUser(user);
        return "redirect:/users/" + user.getId();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return "redirect:/users";
    }
}
