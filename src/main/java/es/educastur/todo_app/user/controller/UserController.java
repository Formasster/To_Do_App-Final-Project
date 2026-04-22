package es.educastur.todo_app.user.controller;

import es.educastur.todo_app.user.dto.CreateUserRequest;
import es.educastur.todo_app.user.model.User;
import es.educastur.todo_app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/auth/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new CreateUserRequest());
        return "register";
    }

    @PostMapping("/auth/register/submit")
    public String processRegisterForm(
            @ModelAttribute("user") CreateUserRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "register";

        User saved = userService.registerUser(request);

        return "redirect:/login";
    }

}
