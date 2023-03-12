package com.skd.service;

import com.skd.dto.InventoryResponse;
import com.skd.dto.OrderLineItemDTO;
import com.skd.dto.OrderRequest;
import com.skd.dto.OrderResponse;
import com.skd.entity.Order;
import com.skd.entity.OrderLineItem;
import com.skd.exception.OrderItemNotInStockException;
import com.skd.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public OrderResponse saveOrder(OrderRequest orderRequest) {

        Order order = Order.builder()
                .orderLineItems(orderRequest.getOrderLineItemDTOS()
                        .stream()
                        .map(orderLineItemDTO -> mapToOrderLineItem(orderLineItemDTO)).collect(Collectors.toList()))
                .orderNumber(UUID.randomUUID().toString())
                .build();

        List<String> skucodes = order.getOrderLineItems().stream()
                .map(orderLineItem -> orderLineItem.getSkucode())
                .collect(Collectors.toList());

        InventoryResponse[] inventoryResponses =  webClient.get().uri("http://localhost:8082/api/v1/inventory/inStock"
                        ,uriBuilder ->uriBuilder.queryParam("skucode", skucodes).build() )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if(allProductsInStock) {
            order.getOrderLineItems().stream().forEach(item -> item.setOrder(order));
            Order savedOrder = orderRepository.save(order);

            return OrderResponse.builder()
                    .orderNumber(savedOrder.getOrderNumber())
                    .orderLineItemDTOS(savedOrder.getOrderLineItems().stream()
                            .map(orderLineItem -> mapToOrderLineItemDto(orderLineItem)).collect(Collectors.toList()))
                    .build();
        }
        else{
            throw new OrderItemNotInStockException("Product is out of stock, please try later");
        }

    }

    private OrderLineItemDTO mapToOrderLineItemDto(OrderLineItem orderLineItem) {
        return OrderLineItemDTO.builder()
                .price(orderLineItem.getPrice())
                .quantity(orderLineItem.getQuantity())
                .skucode(orderLineItem.getSkucode())
                .build();
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemDTO orderLineItemDTO) {
        return OrderLineItem.builder()
                .skucode(orderLineItemDTO.getSkucode())
                .quantity(orderLineItemDTO.getQuantity())
                .price(orderLineItemDTO.getPrice())
                .build();
    }
}
