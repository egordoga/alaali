package ua.alaali_dip.controller;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.alaali_dip.entity.Product;
import ua.alaali_dip.repository.ProductRepository;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
@Controller
public class PictureUpload {

    private String encodedImg;
    byte[] bytes;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/oops1233")
    public String viewOops123(Model model) {
        bytes = null;
        Product product;
        Optional<Product> productOpt = productRepository.findById(35L);
        if (productOpt.isPresent()) {
            product = productOpt.get();
            model.addAttribute("product", product);
            Byte[] arr = product.getPicture2();
            if (arr == null) {
                System.out.println("NULL");
            } else {
                bytes = ArrayUtils.toPrimitive(arr);
                System.out.println(Arrays.toString(arr));
            }
        }
        return "add_success";
    }

    @PostMapping("/oops1233")
    public String handlePictUpload() {
        bytes = null;
        Product product = null;
        Byte[] picture = null;
        System.out.println("GGGGGGG");
        Optional<Product> productOpt = productRepository.findById(35L);
        if (productOpt.isPresent()) {
            product = productOpt.get();
            picture = product.getPictureFirst();
            bytes = ArrayUtils.toPrimitive(picture);
        }

        //encodedImg = Base64.getEncoder().encodeToString(bytes);
        return "redirect:/oops1233";
    }

    @GetMapping("/oo")
    public @ResponseBody
    byte[] gi() {
        return bytes;
    }

}
