package com.sanlam.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sanlam.transfer.client")
public class TransferApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransferApplication.class, args);
	}
}
