package ua.alaali_dip.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String activationCode;
    private Byte active;
    @JsonIgnore
    private String pass;  // Знаю, что имя не хорошее, но для наглядности

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    @JsonManagedReference(value = "rating")
    private Rating rating;

    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "visitor")
    private List<Product> products;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basket_id")
    @JsonBackReference(value = "basket")
    private Basket basket;

    @ManyToMany
    @JoinTable(name = "visitor_role", joinColumns = @JoinColumn(name = "visitor_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference(value = "roles")
    private Collection<Role> roles;
}
