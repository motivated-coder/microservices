package com.skd;

import com.skd.entity.Inventory;
import com.skd.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class,args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository){
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setSkucode("Iphone 14");
            inventory.setQuantity(100);

            Inventory inventory2 = new Inventory();
            inventory2.setSkucode("Iphone 13");
            inventory2.setQuantity(100);

            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory2);
        };
    }
}
