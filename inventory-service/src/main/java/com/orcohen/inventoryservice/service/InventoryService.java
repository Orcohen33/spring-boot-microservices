package com.orcohen.inventoryservice.service;

import com.orcohen.inventoryservice.dto.InventoryRequest;
import com.orcohen.inventoryservice.dto.InventoryResponse;
import com.orcohen.inventoryservice.model.Inventory;
import com.orcohen.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        log.info("Found {} inventories", inventories.size());
        return inventories;
    }
    public void createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = Inventory
                .builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();

        inventoryRepository.save(inventory);

        log.info("Inventory {} created successfully", inventory.getId());
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse
                                .builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).peek(inventoryResponse -> log.info("[isInStock] skuCode: {}, is in stock: {}", inventoryResponse.getSkuCode(), inventoryResponse.isInStock()))
                .collect(Collectors.toList());
    }
}
