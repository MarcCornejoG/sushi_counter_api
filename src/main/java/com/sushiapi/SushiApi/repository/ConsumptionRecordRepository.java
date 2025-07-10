package com.sushiapi.SushiApi.repository;

import com.sushiapi.SushiApi.model.entities.ConsumptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumptionRecordRepository extends JpaRepository<ConsumptionRecord, Long> {

    List<ConsumptionRecord> findByUserId(Long userId);
    List<ConsumptionRecord> findByUserIdAndSushiId(Long userId, Long sushiId);

    @Query("SELECT cr FROM ConsumptionRecord cr WHERE cr.userId = :userId AND cr.sushiId = :sushiId " +
            "AND DATE(cr.consumptionDate) = DATE(:date) ORDER BY cr.createdAt DESC")
    Optional<ConsumptionRecord> findTodayRecordByUserAndSushi(
            @Param("userId") Long userId,
            @Param("sushiId") Long sushiId,
            @Param("date") LocalDateTime date
    );

    @Query("SELECT SUM(cr.quantity) FROM ConsumptionRecord cr WHERE cr.userId = :userId")
    Optional<Integer> getTotalSushiConsumedByUser(@Param("userId") Long userId);

    @Query("SELECT SUM(cr.quantity) FROM ConsumptionRecord cr WHERE cr.userId = :userId " +
            "AND DATE(cr.consumptionDate) = DATE(CURRENT_TIMESTAMP)")
    Optional<Integer> getTodaySushiConsumedByUser(@Param("userId") Long userId);

    @Query("SELECT c.sushi.id, SUM(c.quantity) " +
            "FROM ConsumptionRecord c " +
            "WHERE c.user.id = :userId " +
            "AND c.consumptionDate BETWEEN :startOfDay AND :endOfDay " +
            "GROUP BY c.sushi.id")
    List<Object[]> findTodayConsumptionByType(@Param("userId") Long userId,
                                              @Param("startOfDay") LocalDateTime startOfDay,
                                              @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT s.type, SUM(cr.quantity) FROM ConsumptionRecord cr " +
            "JOIN Sushi s ON cr.sushiId = s.id " +
            "WHERE cr.userId = :userId GROUP BY s.type")
    List<Object[]> getSushiStatsByUser(@Param("userId") Long userId);
}
