package com.insat.software.services;

import com.insat.software.models.BatchTradesAggregation;
import com.insat.software.repositories.BatchTradesAggregationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BatchTradesAggregationService {

    private BatchTradesAggregationRepository batchTradesAggregationRepository;

    public List<BatchTradesAggregation> getTodayTrades() {
        Long todayMidnight = System.currentTimeMillis() - (System.currentTimeMillis() % (24 * 60 * 60 * 1000));
        Long todayPlusMidnight = todayMidnight + (24 * 60 * 60 * 1000);
        log.info("Getting today trades between {} and {}", todayMidnight, todayPlusMidnight);
        return batchTradesAggregationRepository.findAllByCreatedAtBetween(
                todayMidnight,
                todayPlusMidnight
        ).collectList().block();
    }

}
