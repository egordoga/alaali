package ua.alaali_dip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.alaali_dip.entity.NewPost;

@Repository
public interface NewPostRepository extends JpaRepository<NewPost, Long> {
    NewPost findByCityAndNumber(String city, Integer number);
}
