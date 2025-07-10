package com.sushiapi.SushiApi.model.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sushiapi.SushiApi.model.enums.SushiType;
import com.sushiapi.SushiApi.utils.SushiTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sushis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sushi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = SushiTypeConverter.class)
    @Column(name = "type", nullable = false)
    private SushiType type;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "main_ingredient", nullable = false, length = 50)
    private String mainIngredient;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationship with consumption records
    @JsonManagedReference
    @OneToMany(mappedBy = "sushi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsumptionRecord> consumptionRecords = new ArrayList<>();
}
