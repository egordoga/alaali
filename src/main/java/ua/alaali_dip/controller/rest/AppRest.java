package ua.alaali_dip.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.alaali_dip.entity.Basket;
import ua.alaali_dip.entity.Groupp;
import ua.alaali_dip.entity.Product;
import ua.alaali_dip.entity.Visitor;
import ua.alaali_dip.model.ProductDTO;
import ua.alaali_dip.service.IServiceDB;
import ua.alaali_dip.service.ServiceDB;
import ua.alaali_dip.util.ResizeImage;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


/**
 * Т.к. RestController для собственного Android application, обойдемся без HAL
 */
@RestController
@RequestMapping("/api")
public class AppRest {

    private final IServiceDB serviceDB;
    private final ResizeImage resizeImage;

    @Autowired
    public AppRest(IServiceDB serviceDB, ResizeImage resizeImage) {
        this.serviceDB = serviceDB;
        this.resizeImage = resizeImage;
    }

    @GetMapping("/one/{id}")
    public Product getOne(@PathVariable Long id) {
        Product product = serviceDB.findProductById(id);
        product.setPictureFirst(resizeImage.resizeImg(product.getPictureFirst()));
        product.setPicture2(resizeImage.resizeImg(product.getPicture2()));
        product.setPicture3(resizeImage.resizeImg(product.getPicture3()));
        product.setPicture4(resizeImage.resizeImg(product.getPicture4()));
        product.setPicture5(resizeImage.resizeImg(product.getPicture5()));
        return product;
    }

    @GetMapping("/groups")
    public List<Groupp> getGroups() {
        return serviceDB.allGroupps();
    }

    @GetMapping("/products")  // потом будет Page
    public List<Product> getProducts() {
        List<Product> products = serviceDB.findAllProduct();
        for (Product product : products) {
            mockProductPicture(product);
        }
        return products;
    }

    @GetMapping("/products/{sid}")
    public List<Product> getProductsBySectionId(@PathVariable Long sid) {
        List<Product> products = serviceDB.findProductsBySection_Id(sid);
        for (Product product : products) {
            mockProductPicture(product);
        }
        return products;
    }

    @GetMapping("/auth")
    public boolean doAuth(Principal principal) {
        return principal != null;
    }

    @GetMapping("/add_product/{id}/{quantity}")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToBasket(@PathVariable long id, @PathVariable int quantity, Principal principal) {
        if (principal != null) {
            Visitor visitor = serviceDB.findVisitorByEmail(principal.getName());
            Basket basket = serviceDB.findBasketByVisitor(visitor);
            if (basket == null) {
                serviceDB.saveBasket(new Basket(visitor));
            }
            Product product = serviceDB.findProductById(id);
            product.setQuantityForOder(quantity);
            basket.getProducts().add(product);
            serviceDB.saveBasket(basket);
        }
    }

    @GetMapping("/basket")
    public List<ProductDTO> getBasket(Principal principal) {
        List<ProductDTO> list = new ArrayList<>();
        if (principal != null) {
            Visitor visitor = serviceDB.findVisitorByEmail(principal.getName());
            Basket basket = serviceDB.findBasketByVisitor(visitor);
            list = makeListDTO(basket);
        }
        return list;
    }

    @DeleteMapping("basket/{id}")
    public List<ProductDTO> deleteProductFromBasket(@PathVariable Long id, Principal principal) {
        List<ProductDTO> list = new ArrayList<>();
        if (principal != null) {
            Visitor visitor = serviceDB.findVisitorByEmail(principal.getName());
            Basket basket = serviceDB.findBasketByVisitor(visitor);
            for (Product product : basket.getProducts()) {
                if (product.getId().equals(id)) {
                    basket.getProducts().remove(product);
                    serviceDB.saveBasket(basket);
                }
            }
            list = makeListDTO(basket);
        }
        return list;
    }

    private List<ProductDTO> makeListDTO(Basket basket) {
        List<ProductDTO> list = new ArrayList<>();
        for (Product product : basket.getProducts()) {
            ProductDTO dto = ProductDTO.convertToDTO(product);
            list.add(dto);
        }
        return list;
    }


    private void mockProductPicture(Product product) {
        Byte[] smallPict = resizeImage.resizeImg(product.getPictureFirst());
        product.setPictureFirst(smallPict);
        Byte[] mock = new Byte[0];
        product.setPicture2(mock);
        product.setPicture3(mock);
        product.setPicture4(mock);
        product.setPicture5(mock);
    }
}
