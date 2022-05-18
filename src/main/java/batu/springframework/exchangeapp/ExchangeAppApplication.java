package batu.springframework.exchangeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ExchangeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeAppApplication.class, args);
	}
}
