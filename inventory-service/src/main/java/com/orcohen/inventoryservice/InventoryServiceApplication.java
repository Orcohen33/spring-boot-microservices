package com.orcohen.inventoryservice;

import com.orcohen.inventoryservice.model.Inventory;
import com.orcohen.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return (args) -> {
			inventoryRepository.save(
					Inventory.builder()
							.skuCode("iphone_14_pro_max")
							.quantity(100)
							.build());
			inventoryRepository.save(
					Inventory.builder()
							.skuCode("iphone_14_pro")
							.quantity(20)
							.build());
			inventoryRepository.save(
					Inventory.builder()
							.skuCode("iphone_14")
							.quantity(0)
							.build());
		};
	}

}
