package com.skd.repository;

import com.skd.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkucodeIn(List<String> skucodes);

    Optional<Inventory> findBySkucode(String skucode);
}
