package my.project.internetprovider.controllers;

import my.project.internetprovider.models.User;
import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal User authUser) {
        if (authUser == null)
            return "redirect:/items/plans";

        String role = userService.getRoleForUser(authUser);

        if ("ROLE_ADMIN".equals(role))
            return "redirect:/users";
        else if ("ROLE_USER".equals(role))
           return "redirect:/users/" + authUser.getId();

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
