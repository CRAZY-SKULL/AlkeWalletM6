package cl.AlkeWalletM6.AlkeWalletM6.service;

import cl.AlkeWalletM6.AlkeWalletM6.model.Transaction;
import cl.AlkeWalletM6.AlkeWalletM6.model.User;
import cl.AlkeWalletM6.AlkeWalletM6.repository.TransactionRepository;
import cl.AlkeWalletM6.AlkeWalletM6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setTransactionType("deposit");
        transactionRepository.save(transaction);
    }

    public void withdraw(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (hasEnoughFunds(user, amount)) {
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setAmount(amount);
            transaction.setTransactionType("withdraw");
            transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Insufficient funds");
        }
    }

    public void send(Long senderId, Long recipientId, BigDecimal amount) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new RuntimeException("Recipient not found"));

        if (hasEnoughFunds(sender, amount)) {
            Transaction senderTransaction = new Transaction();
            senderTransaction.setUserId(senderId);
            senderTransaction.setAmount(amount);
            senderTransaction.setTransactionType("send");
            transactionRepository.save(senderTransaction);

            Transaction recipientTransaction = new Transaction();
            recipientTransaction.setUserId(recipientId);
            recipientTransaction.setAmount(amount);
            recipientTransaction.setTransactionType("receive");
            transactionRepository.save(recipientTransaction);
        } else {
            throw new RuntimeException("Insufficient funds");
        }
    }

    private boolean hasEnoughFunds(User user, BigDecimal amount) {
        // Implementa la lógica para calcular el balance del usuario a partir de sus transacciones
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equals("deposit")) {
                balance = balance.add(transaction.getAmount());
            } else if (transaction.getTransactionType().equals("withdraw")) {
                balance = balance.subtract(transaction.getAmount());
            } else if (transaction.getTransactionType().equals("send")) {
                balance = balance.subtract(transaction.getAmount());
            } else if (transaction.getTransactionType().equals("receive")) {
                balance = balance.add(transaction.getAmount());
            }
        }
        return balance.compareTo(amount) >= 0;
    }
}