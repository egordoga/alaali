package ua.alaali_dip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.alaali_dip.entity.Basket;
import ua.alaali_dip.entity.NewPost;
import ua.alaali_dip.entity.Product;
import ua.alaali_dip.entity.Visitor;
import ua.alaali_dip.model.OrderForm;
import ua.alaali_dip.service.IServiceDB;
import ua.alaali_dip.service.MailSender;
import ua.alaali_dip.service.ServiceDB;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final IServiceDB serviceDB;
    private final MailSender mailSender;

    @Autowired
    public OrderController(IServiceDB serviceDB, MailSender mailSender) {
        this.serviceDB = serviceDB;
        this.mailSender = mailSender;
    }

    @GetMapping("/order")
    public String viewOrder(Model model, Principal principal) {

        model.addAttribute("order", new OrderForm());

        Visitor visitor = serviceDB.findVisitorByEmail(principal.getName());
        model.addAttribute("visitorAuth", visitor);
        Basket basket = serviceDB.findBasketByVisitor(visitor);
        model.addAttribute("basket", basket);
        model.addAttribute("prods", basket.getProducts());
        model.addAttribute("newPost", basket.getNewPost());

        int sumBasket = basket.getProducts().stream().map(Product::getCost).mapToInt(Integer::intValue).sum();
        model.addAttribute("sum", sumBasket);
        return "order";
    }

    @PostMapping("/order")
    public String addOrder(@ModelAttribute OrderForm orderForm, Model model, Principal principal) {
        Visitor visitor = serviceDB.findVisitorByEmail(principal.getName());
        model.addAttribute("visitorAuth", visitor);
        Basket basket = serviceDB.findBasketByVisitor(visitor);
        model.addAttribute("basket", basket);
        model.addAttribute("order", orderForm);

        NewPost newPost = serviceDB.findNewPost(orderForm.getCity(), orderForm.getNumber());

        /*
         * Конечно, я понимаю, что тут должна быть база с не изменяемым списком Новых почт, а только проверяемым на
         * существование, но пока так...
         */
        if (newPost == null) {
            newPost = new NewPost(orderForm.getCity(), orderForm.getNumber());
            serviceDB.saveNewPost(newPost);
        }

        basket.setNewPost(newPost);
        basket.setRecipient(orderForm.getRecipient());
        basket.setRecipientPhone(orderForm.getRecipientPhone());

        sendMailOrders(basket.getProducts(), visitor, orderForm.getRecipient(), orderForm.getRecipientPhone(),
                newPost.getCity(), String.valueOf(newPost.getNumber()));

        basket.getProducts().clear(); //Логическое окончание работы программы. Это стоило делать после оплаты
        serviceDB.saveBasket(basket);

        return "redirect:/end";
    }

    private void sendMailOrders(List<Product> products, Visitor byer, String recip, String recipPhone,
                                String postCity, String postNum) {
        Map<Long, ArrayList<Product>> sellers = new HashMap<>();
        for (Product product : products) {
            Long key = product.getVisitor().getId();
            if (sellers.containsKey(key)) {
                sellers.get(key).add(product);
            } else {
                ArrayList<Product> prods = new ArrayList<>();
                prods.add(product);
                sellers.put(key, prods);
            }
        }

        for (ArrayList<Product> prods : sellers.values()) {
            StringBuilder mail = new StringBuilder();
            mail.append("Уважаемый ").append(prods.get(0).getVisitor().getName())
                    .append("!\nВ Ваш магазин поступил заказ от покупателя ")
                    .append(byer.getName()).append(" по следующим позициям:\n\n")
                    .append("Наименование\tКоличесво\tЦена\n");

            for (Product prod : prods) {
                mail.append(prod.getName()).append("\t")
                        .append(prod.getQuantityForOder()).append("\t")
                        .append(prod.getCost()).append("\n");
            }

            mail.append("\nРеквизиты покупателя:\n")
                    .append("Телефон:\t").append(byer.getPhone()).append("\n")
                    .append("Email:\t").append(byer.getEmail())
                    .append("\n\nРеквизиты получателя:\n")
                    .append("Получатель:\t").append(recip).append("\n")
                    .append("Телефон:\t").append(recipPhone).append("\n")
                    .append("Новая почта №:\t").append(postNum).append("\n")
                    .append("Город:\t").append(postCity).append("\n")
                    .append("\n\nС уважением. AlaAli");

            mailSender.sendMail(prods.get(0).getVisitor().getEmail(), "Заказ покупателя", mail.toString());
        }
    }
}
