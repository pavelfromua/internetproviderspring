package my.project.internetprovider.controllers;

import my.project.internetprovider.services.InstallService;
import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class MainController {
    private final InstallService installService;
    private final UserService userService;

    @Autowired
    public MainController(InstallService installService, UserService userService) {
        this.installService = installService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal == null)
            return "index";

        String role = userService.getCurrentRole(principal.getName());

        if ("ADMIN".equals(role))
            return "redirect:/admin";
        else if ("USER".equals(role))
            return "redirect:/cabinet";

        return "index";
    }

    @GetMapping("/install")
    public String install(Model model) {
        installService.loadInitDataIntoDB();

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }


//        @GetMapping("/main")
//        public String main(Map<String, Object> model) {
//            Iterable<Message> messages = messageRepo.findAll();
//
//            model.put("messages", messages);
//
//            return "main";
//        }

//        @PostMapping("/main")
//        public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
//            Message message = new Message(text, tag);
//
//            messageRepo.save(message);
//
//            Iterable<Message> messages = messageRepo.findAll();
//
//            model.put("messages", messages);
//
//            return "main";
//        }

//        @PostMapping("filter")
//        public String filter(@RequestParam String filter, Map<String, Object> model) {
//            Iterable<Message> messages;
//
//            if (filter != null && !filter.isEmpty()) {
//                messages = messageRepo.findByTag(filter);
//            } else {
//                messages = messageRepo.findAll();
//            }
//
//            model.put("messages", messages);
//
//            return "main";
//        }
    //}
}
