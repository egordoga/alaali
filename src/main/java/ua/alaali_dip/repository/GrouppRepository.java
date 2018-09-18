package ua.alaali_dip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.alaali_dip.entity.Groupp;

@Repository
public interface GrouppRepository extends JpaRepository<Groupp, Long> {
}
