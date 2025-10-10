package com.sanlam.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.sanlam.transfer.client")
@EnableScheduling
public class TransferApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransferApplication.class, args);
	}
}
