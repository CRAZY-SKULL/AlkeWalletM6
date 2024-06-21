package cl.AlkeWalletM6.AlkeWalletM6.controller;

import org.springframework.ui.Model;
import cl.AlkeWalletM6.AlkeWalletM6.model.Transaction;
import cl.AlkeWalletM6.AlkeWalletM6.model.User;
import cl.AlkeWalletM6.AlkeWalletM6.service.TransactionService;
import cl.AlkeWalletM6.AlkeWalletM6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listTransactions(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Transaction> transactions = transactionService.findByUserId(user.getId());
        model.addAttribute("transactions", transactions);
        return "home";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("amount") BigDecimal amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        transactionService.deposit(user.getId(), amount);
        return "redirect:/home";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("amount") BigDecimal amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        transactionService.withdraw(user.getId(), amount);
        return "redirect:/home";
    }

    @PostMapping("/send")
    public String send(@RequestParam("recipientUsername") String recipientUsername, @RequestParam("amount") BigDecimal amount, Principal principal) {
        User sender = userService.findByUsername(principal.getName());
        User recipient = userService.findByUsername(recipientUsername);
        transactionService.send(sender.getId(), recipient.getId(), amount);
        return "redirect:/home";
    }
}
