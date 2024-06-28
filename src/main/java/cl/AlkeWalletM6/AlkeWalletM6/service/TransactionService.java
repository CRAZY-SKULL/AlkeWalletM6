package cl.AlkeWalletM6.AlkeWalletM6.service;

import cl.AlkeWalletM6.AlkeWalletM6.model.Transaction;
import cl.AlkeWalletM6.AlkeWalletM6.model.User;
import cl.AlkeWalletM6.AlkeWalletM6.repository.TransactionRepository;
import cl.AlkeWalletM6.AlkeWalletM6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = createTransaction(user, amount, "deposit");
        transactionRepository.save(transaction);
    }

    @Transactional
    public void withdraw(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!hasEnoughFunds(user, amount)) {
            throw new RuntimeException("Insufficient funds");
        }

        Transaction transaction = createTransaction(user, amount, "withdraw");
        transactionRepository.save(transaction);
    }

    @Transactional
    public void send(Long senderId, Long recipientId, BigDecimal amount) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        if (!hasEnoughFunds(sender, amount)) {
            throw new RuntimeException("Insufficient funds");
        }

        Transaction senderTransaction = createTransaction(sender, amount, "send");
        transactionRepository.save(senderTransaction);

        Transaction recipientTransaction = createTransaction(recipient, amount, "receive");
        transactionRepository.save(recipientTransaction);
    }

    private Transaction createTransaction(User user, BigDecimal amount, String transactionType) {
        Transaction transaction = new Transaction();
        transaction.setUserId(user.getId());
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        return transaction;
    }

    private boolean hasEnoughFunds(User user, BigDecimal amount) {
        BigDecimal balance = calculateBalance(user.getId());
        return balance.compareTo(amount) >= 0;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return calculateBalanceFromTransactions(transactions);
    }

    private BigDecimal calculateBalanceFromTransactions(List<Transaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            switch (transaction.getTransactionType()) {
                case "deposit":
                case "receive":
                    balance = balance.add(transaction.getAmount());
                    break;
                case "withdraw":
                case "send":
                    balance = balance.subtract(transaction.getAmount());
                    break;
                default:
                    // Handle unexpected transaction types if needed
                    break;
            }
        }

        return balance;
    }
}
