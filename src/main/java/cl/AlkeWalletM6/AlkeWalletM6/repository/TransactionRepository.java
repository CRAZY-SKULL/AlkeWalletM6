package cl.AlkeWalletM6.AlkeWalletM6.repository;

import cl.AlkeWalletM6.AlkeWalletM6.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
}