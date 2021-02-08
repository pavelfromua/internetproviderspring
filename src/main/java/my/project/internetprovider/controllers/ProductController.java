package my.project.internetprovider.controllers;

import my.project.internetprovider.models.Product;
import my.project.internetprovider.models.User;
import my.project.internetprovider.services.ProductService;
import my.project.internetprovider.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;

@Controller
@RequestMapping("/items")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService,
                             UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping()
    public String showProducts(Model model) {
        return "redirect:/items/products";
    }

    @GetMapping("/products")
    public String index(Model model, @AuthenticationPrincipal User authUser) {
        model.addAttribute("products", productService.getProducts());
        model.addAttribute("user_role", userService.getRoleForUser(authUser));

        return "items/products/list";
    }

    @GetMapping("/products/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String newProduct(@ModelAttribute("product") Product product) {
        return "/items/products/new";
    }

    @PostMapping("/products")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String create(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "items/products/new";
        }

        productService.saveProduct(product);

        return "redirect:/items/products";
    }

    @GetMapping("/products/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("product", productService.getProductById(id));

        return "items/products/edit";
    }

    @PatchMapping("/products/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@ModelAttribute("product") @Valid Product product,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "items/products/edit";

        productService.updateProduct(product);

        return "redirect:/items/products";
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        productService.deleteProduct(id);

        return "redirect:/items/products";
    }
}
