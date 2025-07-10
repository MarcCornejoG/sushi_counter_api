package com.sushiapi.SushiApi.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionRequestDTO {
    private Long userId;
    private Long sushiId;
    private Integer quantity;
}
