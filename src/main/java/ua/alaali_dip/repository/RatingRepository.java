package ua.alaali_dip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.alaali_dip.entity.Rating;
import ua.alaali_dip.entity.Visitor;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findByVisitor(Visitor visitor);

    @Query("select r from Rating r")
    List<Rating> selectAll();
}
