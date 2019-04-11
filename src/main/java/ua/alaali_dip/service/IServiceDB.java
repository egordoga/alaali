package ua.alaali_dip.service;

import ua.alaali_dip.entity.*;

import java.util.List;

public interface IServiceDB {
    boolean addVisitor(Visitor visitor, String roleName);

    Product addProduct(Product product);

    void removeProduct(Product product);

    List<Groupp> allGroupps();

    List<Section> allSection();

    List<Section> findSectionByGroupp(long id);

    Role findRoleByName(String name);

    Visitor findVisitorByEmail(String email);

    List<Product> findAllBySection(Section section);

    List<Product> findAllBySellerId(Long id);

    Section findSectionById(long id);

    Product findProductById(long id);

    Visitor findVisitorById(Long id);

    Visitor saveVisitor(Visitor visitor, String roleName);

    Basket saveBasket(Basket basket);

    List<Product> findProductsByBasket(Basket basket);

    Basket findBasketByVisitor(Visitor visitor);

    NewPost findNewPost(String city, Integer number);

    NewPost saveNewPost(NewPost newPost);

    List<Product> findProductByString(String string);

    void saveRating(Integer rating, Visitor visitor);

    List<Product> findAllProduct();

    List<Product> findProductsBySection_Id(Long id);

    boolean activateVisitor(String code);
}
