package ua.alaali_dip.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.alaali_dip.entity.Groupp;
import ua.alaali_dip.entity.Product;
import ua.alaali_dip.service.ServiceDB;
import ua.alaali_dip.util.ResizeImage;

import java.security.Principal;
import java.util.List;


/**
 * Т.к. RestController для собственного Android application, обойдемся без HAL
 */
@RestController
@RequestMapping("/api")
public class AppRest {

    private final ServiceDB serviceDB;
    private final ResizeImage resizeImage;

    @Autowired
    public AppRest(ServiceDB serviceDB, ResizeImage resizeImage) {
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
