package ua.alaali_dip.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.alaali_dip.entity.*;
import ua.alaali_dip.service.IServiceDB;
import ua.alaali_dip.service.ServiceDB;

import java.security.Principal;
import java.util.List;

@Controller
public class CardController {

    private final IServiceDB serviceDB;

    @Autowired
    public CardController(IServiceDB serviceDB) {
        this.serviceDB = serviceDB;
    }

    @GetMapping(value = "/main")
    public String showMain(@ModelAttribute("sectId") String strId, Model model, Principal principal) {
        model.addAttribute("groupps", serviceDB.allGroupps());
        model.addAttribute("groupp", new Groupp());
        model.addAttribute("serviceDB", serviceDB);
        model.addAttribute("section", new Section());
        model.addAttribute("picId", "");
        boolean isSeller = false;
        if (principal != null) {
            Visitor visitor = serviceDB.findVisitorByEmail(principal.getName());
            model.addAttribute("visitorAuth", visitor);
            for (Role role : visitor.getRoles()) {
                if (role.getName().equals("seller")) {
                    isSeller = true;
                }
            }
        }
        List<Product> products;
        if (strId == null || "".equals(strId)) {
            products = serviceDB.findAllProduct();
        } else {
            Section section = serviceDB.findSectionById(Long.parseLong(strId));
            products = serviceDB.findAllBySection(section);
        }
        model.addAttribute("products", products);
        model.addAttribute("seller", isSeller);
        return "main";
    }

    @GetMapping("/seller_products")
    public String viewSellerProducts(@ModelAttribute("sellerId") String sellerId, Model model, Principal principal) {
        Long sId = Long.parseLong(sellerId);
        List<Product> products = serviceDB.findAllBySellerId(sId);
        Visitor visitor = serviceDB.findVisitorById(sId);
        Visitor auth = null;
        if (principal != null) {
            auth = serviceDB.findVisitorByEmail(principal.getName());
        }
        model.addAttribute("products", products);
        model.addAttribute("visitor", visitor);
        model.addAttribute("visitorAuth", auth);
        Rating rating = visitor.getRating();
        if (rating != null) {
            model.addAttribute("rating", Math.round(rating.getValue() * 100) / 100.0d);
        } else {
            model.addAttribute("rating", 0);
        }
        return "seller_products";
    }

    @GetMapping("/pict")
    public @ResponseBody
    byte[] showPictureFirst(@ModelAttribute("picId") String strId) {
        Long id = Long.parseLong(strId);
        Product product = serviceDB.findProductById(id);
        return ArrayUtils.toPrimitive(product.getPictureFirst());
    }

    @PostMapping("/find")
    public void findByString(@ModelAttribute("str") String find, Model model) {
        List<Product> products = serviceDB.findProductByString(find);
        viewFind(model, products);
    }

    @GetMapping("/find")
    public String viewFind(Model model, List<Product> products) {
        model.addAttribute("prods", products);
        return "find";
    }

    @GetMapping("/rating")
    public String addRating(@ModelAttribute("rat") String rat, @ModelAttribute("sellerId") String sellerId) {

        Visitor visitor = serviceDB.findVisitorById(Long.parseLong(sellerId));
        serviceDB.saveRating(Integer.valueOf(rat), visitor);

        return "redirect:/seller_products?sellerId=" + sellerId;
    }
}
