package com.sushiapi.SushiApi.service;

import com.sushiapi.SushiApi.model.entities.ConsumptionRecord;
import com.sushiapi.SushiApi.repository.ConsumptionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConsumptionRecordService {

    private final ConsumptionRecordRepository consumptionRecordRepository;

    public ConsumptionRecord recordSushiConsumption(Long userId, Long sushiId, Integer quantity) {
        LocalDateTime today = LocalDateTime.now();

        Optional<ConsumptionRecord> existingRecord = consumptionRecordRepository
                .findTodayRecordByUserAndSushi(userId, sushiId, today);

        if (existingRecord.isPresent()) {
            ConsumptionRecord record = existingRecord.get();
            record.setQuantity(record.getQuantity() + quantity);
            return consumptionRecordRepository.save(record);
        } else {
            ConsumptionRecord newRecord = new ConsumptionRecord(userId, sushiId, quantity);
            return consumptionRecordRepository.save(newRecord);
        }
    }

    public ConsumptionRecord incrementSushiConsumption(Long userId, Long sushiId) {
        return recordSushiConsumption(userId, sushiId, 1);
    }

    public ConsumptionRecord decrementSushiConsumption(Long userId, Long sushiId) {
        LocalDateTime today = LocalDateTime.now();

        Optional<ConsumptionRecord> existingRecord = consumptionRecordRepository
                .findTodayRecordByUserAndSushi(userId, sushiId, today);

        if (existingRecord.isPresent()) {
            ConsumptionRecord record = existingRecord.get();

            if (record.getQuantity() > 0) {
                record.setQuantity(record.getQuantity() - 1);
                return consumptionRecordRepository.save(record);
            } else {
                return record;
            }
        } else {
            ConsumptionRecord newRecord = new ConsumptionRecord(userId, sushiId, 0);
            return consumptionRecordRepository.save(newRecord);
        }
    }

    public List<Object[]> getTodayConsumptionByType(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        return consumptionRecordRepository.findTodayConsumptionByType(userId, startOfDay, endOfDay);
    }

    public List<ConsumptionRecord> getUserConsumptionRecords(Long userId) {
        return consumptionRecordRepository.findByUserId(userId);
    }

    public Integer getTotalSushiConsumed(Long userId) {
        return consumptionRecordRepository.getTotalSushiConsumedByUser(userId).orElse(0);
    }

    public Integer getTodaySushiConsumed(Long userId) {
        return consumptionRecordRepository.getTodaySushiConsumedByUser(userId).orElse(0);
    }

    public List<Object[]> getSushiStatsByUser(Long userId) {
        return consumptionRecordRepository.getSushiStatsByUser(userId);
    }
}
