package com.sushiapi.SushiApi.model.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumption_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "sushi_id", nullable = false)
    private Long sushiId;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "consumption_date")
    private LocalDateTime consumptionDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sushi_id", insertable = false, updatable = false)
    private Sushi sushi;


    public ConsumptionRecord(Long userId, Long sushiId, Integer quantity) {
        this.userId = userId;
        this.sushiId = sushiId;
        this.quantity = quantity;
        this.consumptionDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
}
