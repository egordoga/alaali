package ua.alaali_dip.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pass;  // Знаю, что имя не хорошее, но для наглядности
    private String email;
    private String phone;
    private String activationCode;
    private Byte active;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @OneToMany(mappedBy = "visitor", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @ManyToMany
    @JoinTable(name = "visitor_role", joinColumns = @JoinColumn(name = "visitor_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    /*@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "visitor_id"))*/
}
