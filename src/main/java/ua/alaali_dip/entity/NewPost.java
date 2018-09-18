package ua.alaali_dip.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Считаем, что доставка только Новой почтой.
 * Если успею, добавлю адресную доставку
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class NewPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;
    private String city;

    @OneToMany(mappedBy = "newPost", cascade = CascadeType.ALL)
    private List<Basket> baskets;

    public NewPost(String city, Integer number) {
        this.number = number;
        this.city = city;
    }
}
