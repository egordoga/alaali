package ua.alaali_dip.controller;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.alaali_dip.entity.Basket;
import ua.alaali_dip.entity.Product;
import ua.alaali_dip.entity.Section;
import ua.alaali_dip.entity.Visitor;
import ua.alaali_dip.service.ServiceDB;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    private Product product;
    private Basket basket;


    private final ServiceDB serviceDB;

    @Autowired
    public ProductController(ServiceDB serviceDB) {
        this.serviceDB = serviceDB;
    }

    @GetMapping("/add_product")
    public String viewAddProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("section", new Section());
        model.addAttribute("sections", serviceDB.allSection());
        return "add_product";
    }

    @PostMapping("/add_product")
    public String addProduct(@ModelAttribute Product product, Model model) {
        model.addAttribute("product", product);
        try {
            product.setPictureFirst(ArrayUtils.toObject(product.getFiles()[0].getBytes()));
            product.setPicture2(ArrayUtils.toObject(product.getFiles()[1].getBytes()));
            product.setPicture3(ArrayUtils.toObject(product.getFiles()[2].getBytes()));
            product.setPicture4(ArrayUtils.toObject(product.getFiles()[3].getBytes()));
            product.setPicture5(ArrayUtils.toObject(product.getFiles()[4].getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        serviceDB.addProduct(product);
        return "redirect:/add_success";
    }

    @GetMapping("/product")
    public String viewProduct(Model model, @ModelAttribute("prodId") String prodId, Principal principal) {
        product = serviceDB.findProductById(Long.parseLong(prodId));
        model.addAttribute("product", product);
        if (principal != null) {
            Visitor visitorAuth = serviceDB.findVisitorByEmail(principal.getName());
            model.addAttribute("visitorAuth", visitorAuth);
        }
        return "product";
    }

    @GetMapping("/prodPict")
    public @ResponseBody
    byte[] getProductPicture(@ModelAttribute("pictNum") String pictNum) {
        switch (pictNum) {
            case "2":
                return ArrayUtils.toPrimitive(product.getPicture2());
            case "3":
                return ArrayUtils.toPrimitive(product.getPicture3());
            case "4":
                return ArrayUtils.toPrimitive(product.getPicture4());
            case "5":
                return ArrayUtils.toPrimitive(product.getPicture5());
        }
        return ArrayUtils.toPrimitive(product.getPictureFirst());
    }

    @GetMapping("/basket")
    public String viewBasket(Model model, Principal principal) {
        Visitor visitor;
        List<Product> products;
        if (principal != null) {
            visitor = serviceDB.findVisitorByEmail(principal.getName());
            basket = serviceDB.findBasketByVisitor(visitor);
            if (basket == null) {
                serviceDB.saveBasket(new Basket(visitor));
            }

            products = serviceDB.findProductsByBasket(visitor.getBasket());
            model.addAttribute("visitorAuth", visitor);
            model.addAttribute("prods", products);

            int sumBasket = basket.getProducts().stream().map(Product::getCost).mapToInt(Integer::intValue).sum();
            model.addAttribute("sum", sumBasket);
        }
        return "/basket";
    }

    @GetMapping("/add_to_basket")
    public String addToBasket(Principal principal, @ModelAttribute("prodId") String prodId, @ModelAttribute("quant") String quant) {
        Visitor visitor;
        if (principal != null) {
            visitor = serviceDB.findVisitorByEmail(principal.getName());
            basket = serviceDB.findBasketByVisitor(visitor);
            if (basket == null) {
                serviceDB.saveBasket(new Basket(visitor));
            }
            Product product = serviceDB.findProductById(Long.parseLong(prodId));
            product.setQuantityForOder(Integer.parseInt(quant));
            basket.getProducts().add(product);
            serviceDB.saveBasket(basket);
        }
        return "redirect:/main";
    }

    @GetMapping("/del_from_basket")
    public String delFromBasket(Model model, Principal principal, @ModelAttribute("prodDelName") String prodDel) {
        Visitor visitor;
        if (principal != null) {
            visitor = serviceDB.findVisitorByEmail(principal.getName());
            model.addAttribute("visitorAuth", visitor);
            basket = serviceDB.findBasketByVisitor(visitor);
        }
        for (int i = 0; i < basket.getProducts().size(); i++) {
            if (basket.getProducts().get(i).getName().equals(prodDel)) {
                basket.getProducts().remove(i);
                serviceDB.saveBasket(basket);
                break;
            }
        }
        return "redirect:/basket";
    }
}
