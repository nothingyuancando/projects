package com.spring.request;

import lombok.Data;

@Data
public class PlanAcceptAndRejectRequest {

    private Integer plan_id;
    private Integer factory_id;
    private Integer plan_states;
}
