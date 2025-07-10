package com.sushiapi.SushiApi.controller;

import com.sushiapi.SushiApi.model.dtos.ConsumptionRequestDTO;
import com.sushiapi.SushiApi.model.entities.ConsumptionRecord;
import com.sushiapi.SushiApi.service.ConsumptionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ConsumptionRecordController {

    private final ConsumptionRecordService consumptionRecordService;

    @PostMapping("/increment")
    public ResponseEntity<ConsumptionRecord> incrementSushiConsumption(
            @RequestParam Long userId,
            @RequestParam Long sushiId) {

        try {
            ConsumptionRecord record = consumptionRecordService.incrementSushiConsumption(userId, sushiId);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/decrement")
    public ResponseEntity<ConsumptionRecord> decrementSushiConsumption(
            @RequestParam Long userId,
            @RequestParam Long sushiId) {

        try {
            ConsumptionRecord record = consumptionRecordService.decrementSushiConsumption(userId, sushiId);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/record")
    public ResponseEntity<ConsumptionRecord> recordConsumption(@RequestBody ConsumptionRequestDTO request) {
        try {
            ConsumptionRecord record = consumptionRecordService.recordSushiConsumption(
                    request.getUserId(),
                    request.getSushiId(),
                    request.getQuantity() != null ? request.getQuantity() : 1
            );
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

//TODO
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConsumptionRecord>> getUserConsumption(@PathVariable Long userId) {
        System.out.println("=== CALLED /user/" + userId + " ===");

        List<ConsumptionRecord> records = consumptionRecordService.getUserConsumptionRecords(userId);
        return ResponseEntity.ok(records);
    }


    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Integer> getTotalConsumption(@PathVariable Long userId) {
        System.out.println("=== CALLED /user/" + userId + "/total ===");

        Integer total = consumptionRecordService.getTotalSushiConsumed(userId);
        return ResponseEntity.ok(total);
    }


    @GetMapping("/user/{userId}/today")
    public ResponseEntity<Integer> getTodayConsumption(@PathVariable Long userId) {
        Integer today = consumptionRecordService.getTodaySushiConsumed(userId);
        return ResponseEntity.ok(today);
    }

    @GetMapping("/user/{userId}/today-by-type")
    public ResponseEntity<List<Object[]>> getTodayConsumptionByType(@PathVariable Long userId) {
        List<Object[]> todayByType = consumptionRecordService.getTodayConsumptionByType(userId);
        return ResponseEntity.ok(todayByType);
    }

    //TODO
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<List<Object[]>> getSushiStats(@PathVariable Long userId) {
        List<Object[]> stats = consumptionRecordService.getSushiStatsByUser(userId);
        return ResponseEntity.ok(stats);
    }
}
