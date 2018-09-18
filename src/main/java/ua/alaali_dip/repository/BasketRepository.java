package ua.alaali_dip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.alaali_dip.entity.Basket;
import ua.alaali_dip.entity.Visitor;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByVisitor(Visitor visitor);
}
