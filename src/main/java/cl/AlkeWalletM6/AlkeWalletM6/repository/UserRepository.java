package cl.AlkeWalletM6.AlkeWalletM6.repository;

import cl.AlkeWalletM6.AlkeWalletM6.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
