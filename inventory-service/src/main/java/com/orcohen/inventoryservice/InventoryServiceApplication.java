package com.orcohen.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


//	@Bean
//	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
//		return (args) -> {
//			inventoryRepository.save(
//					Inventory.builder()
//							.skuCode("iphone_14_pro_max")
//							.quantity(100)
//							.build());
//			inventoryRepository.save(
//					Inventory.builder()
//							.skuCode("iphone_14_pro")
//							.quantity(20)
//							.build());
//			inventoryRepository.save(
//					Inventory.builder()
//							.skuCode("iphone_14")
//							.quantity(0)
//							.build());
//		};
//	}

}
