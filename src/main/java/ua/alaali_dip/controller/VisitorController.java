package ua.alaali_dip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.alaali_dip.entity.Visitor;
import ua.alaali_dip.service.IServiceDB;
import ua.alaali_dip.service.ServiceDB;

@Controller
public class VisitorController {

    private String roleName;

    private final IServiceDB serviceDB;

    @Autowired
    public VisitorController(IServiceDB serviceDB) {
        this.serviceDB = serviceDB;
    }

    @GetMapping("/registration")
    public String viewRegistration(Model model) {
        Visitor visitor = new Visitor();
        model.addAttribute("visitor", visitor);
        return "registration";
    }

    @GetMapping("/activation")
    public String viewActivation() {
        return "activation";
    }

    @GetMapping("/act")
    public String addBuyer() {
        return "main";
    }


    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = serviceDB.activateVisitor(code);

        if (isActivated) {
            model.addAttribute("message", "Вы успешно прошли активацию");
        } else {
            model.addAttribute("message", "Код активации не найден");
        }

        return "activate";
    }


    @GetMapping("/seller")
    public String viewRegSeller(@ModelAttribute Visitor visitor, Model model) {
        model.addAttribute("visitorRole", "продавца");
        model.addAttribute("typeName", "Введите название предприятия или ФЛП");
        roleName = "seller";
        return "registration";
    }

    @GetMapping("/buyer")
    public String viewRegBuyer(@ModelAttribute Visitor visitor, Model model) {
        model.addAttribute("visitorRole", "покупателя");
        model.addAttribute("typeName", "Введите фамилию и имя");
        roleName = "buyer";
        return "registration";
    }

    @PostMapping("/visitor")
    public String handlerRegistration(@ModelAttribute Visitor visitor, Model model) {
        model.addAttribute("visitor", visitor);

        if (serviceDB.findVisitorByEmail(visitor.getEmail()) != null) {
            model.addAttribute("message", "Пользователь с таким электронным ящиком уже существует");
            return "activate";
        } else {
            serviceDB.addVisitor(visitor, roleName);
        }
        return "redirect:/activation";
    }

    @GetMapping("/login")
    public String viewLogin(Model model) {
        model.addAttribute("visitor", new Visitor());
        return "login";
    }
}


