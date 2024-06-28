package cl.AlkeWalletM6.AlkeWalletM6;

import cl.AlkeWalletM6.AlkeWalletM6.model.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
//@ComponentScan(basePackageClasses = {cl.AlkeWalletM6.AlkeWalletM6.})
public class AlkeWalletM6Application {

	public static void main(String[] args) {
		SpringApplication.run(AlkeWalletM6Application.class, args);

	}

}
