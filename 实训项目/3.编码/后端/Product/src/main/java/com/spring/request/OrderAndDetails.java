package com.spring.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderAndDetails {

    private Integer order_id;
    private Integer order_states;
    private List<OrderDetailRequest> order_details;

    // Getters and setters

    @Data
    public static class OrderDetailRequest {
        private Integer product_id;
        private Integer amount;
        private Integer order_id;

        // Getters and setters
    }
}
