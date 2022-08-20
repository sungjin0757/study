package basic.redisstudy.cache.repository;

import basic.redisstudy.cache.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
