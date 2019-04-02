package ua.alaali_dip.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private String recipientPhone;

    @ManyToMany
    @JoinTable(name = "basket_product", joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonManagedReference(value = "baskets")
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "basket")
    @JsonManagedReference(value = "basket")
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name = "new_post_id")
    @JsonManagedReference(value = "newPost")
    private NewPost newPost;

    public Basket(Visitor visitor) {
        this.visitor = visitor;
    }
}
