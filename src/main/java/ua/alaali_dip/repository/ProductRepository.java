package ua.alaali_dip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.alaali_dip.entity.Basket;
import ua.alaali_dip.entity.Product;
import ua.alaali_dip.entity.Section;
import ua.alaali_dip.entity.Visitor;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllBySection(Section section);

    List<Product> findAllByVisitor(Visitor visitor);

    List<Product> findAllByBaskets(Basket basket);

    @Query("SELECT p FROM Product p WHERE p.name LIKE :str")
    List<Product> findAllByString(@Param("str") String string);
}
