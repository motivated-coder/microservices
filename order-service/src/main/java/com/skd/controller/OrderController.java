package com.skd.controller;

import com.skd.dto.OrderRequest;
import com.skd.dto.OrderResponse;
import com.skd.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/placeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse =  orderService.saveOrder(orderRequest);
        return "Order Successfully Saved with number "+orderResponse.getOrderNumber();
    }
}
