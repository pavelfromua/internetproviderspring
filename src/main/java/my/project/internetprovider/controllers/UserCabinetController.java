package my.project.internetprovider.controllers;

import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequestMapping("/cabinet")
public class UserCabinetController {
    private final UserService userService;

    @Autowired
    public UserCabinetController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model) {
        return "cabinet";
    }

    @GetMapping("/userinfo")
    public String showUserInfo(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByLogin(principal.getName()));
        model.addAttribute("user_role", userService.getCurrentRole(principal.getName()));

        return "users/show";
    }
}
