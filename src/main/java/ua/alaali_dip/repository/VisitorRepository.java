package ua.alaali_dip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.alaali_dip.entity.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Visitor findByActivationCode(String code);

    Visitor findByName(String name);

    Visitor findByEmail(String email);
}
