package my.project.internetprovider.controllers;

import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MyErrorController implements ErrorController {
    private final UserService userService;

    @Autowired
    public MyErrorController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Principal principal) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null || principal == null)
            return "error";

        model.addAttribute("user_role", userService.getCurrentRole(principal.getName()));

        Integer statusCode = Integer.valueOf(status.toString());

        if (statusCode == HttpStatus.NOT_FOUND.value())
            return "errors/404";
        else if (statusCode == HttpStatus.FORBIDDEN.value())
            return "errors/403";
        else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
            return "errors/500";
        else
            return "errors/other";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}