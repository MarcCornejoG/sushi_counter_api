package com.sushiapi.SushiApi.repository;

import com.sushiapi.SushiApi.model.entities.Sushi;
import com.sushiapi.SushiApi.model.enums.SushiType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SushiRepository extends JpaRepository<Sushi,Long> {
    List<Sushi> findByType(SushiType type);
    List<Sushi> findByMainIngredientIgnoreCase(String ingredient);

    List<Sushi> findByNameContainingIgnoreCase(String namePart);
}
