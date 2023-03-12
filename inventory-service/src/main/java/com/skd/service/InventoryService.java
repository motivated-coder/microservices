package com.skd.service;

import com.skd.dto.InventoryResponse;
import com.skd.entity.Inventory;
import com.skd.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skucodes) {
        return inventoryRepository.findBySkucodeIn(skucodes)
                .stream().map(inventory -> mapToInventoryResponse(inventory))
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .skucode(inventory.getSkucode())
                .isInStock(inventory.getQuantity()>0)
                .build();
    }
}
