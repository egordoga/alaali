package ua.alaali_dip.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer quantity;         //понятно, что лучше Double. а еще лучше BigDecimal, но пока для упрощения так
    private Integer cost;
    private Integer quantityForOder;
    private String description;


    @Lob
    private Byte[] pictureFirst;
    @Lob
    private Byte[] picture2;        //пробовал Byte[][], но не получилось
    @Lob
    private Byte[] picture3;
    @Lob
    private Byte[] picture4;
    @Lob
    private Byte[] picture5;


    @ManyToOne
    @JoinColumn(name = "section_id")
    @JsonManagedReference(value = "section")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    @JsonBackReference(value = "visitor")
    private Visitor visitor;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "basket_product", joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "basket_id"))
    @JsonBackReference(value = "baskets")
    private List<Basket> baskets;

    @JsonIgnore
    @Transient
    private MultipartFile[] files;
}
