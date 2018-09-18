package ua.alaali_dip.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.alaali_dip.entity.Visitor;

@Controller
public class CommonController {

    @GetMapping("/header")
    public String viewHeader(@AuthenticationPrincipal Visitor visitor, Model model) {
        model.addAttribute("visitorAuth", visitor);
        return "header";
    }
}
