package techieamit_it.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techieamit_it.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}