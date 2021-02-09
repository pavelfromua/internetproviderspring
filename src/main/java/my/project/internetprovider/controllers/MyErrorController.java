package my.project.internetprovider.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Principal principal) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status == null || principal == null)
            return "error";

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
