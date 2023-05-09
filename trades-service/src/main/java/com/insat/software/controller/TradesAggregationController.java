package com.insat.software.controller;

import com.insat.software.models.BatchTradesAggregation;
import com.insat.software.models.TradesAggregation;
import com.insat.software.services.BatchTradesAggregationService;
import com.insat.software.services.TradeAggregationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class TradesAggregationController {

    private TradeAggregationService tradeAggregationService;
    private BatchTradesAggregationService batchTradesAggregationService;

    @GetMapping("/symbols")
    public List<String> getSymbols() {
        log.info("Getting symbols");
        return tradeAggregationService.getSymbols();
    }

    @GetMapping
    public Flux<TradesAggregation> getTrades(
            @RequestParam("symbols") List<String> symbols
    ) {
        log.info("Getting trades for symbols: {}", symbols);
        return tradeAggregationService.findTrades(symbols);
    }

    @GetMapping("latest")
    public Mono<Page<TradesAggregation>> getLatestTrades(
            @RequestParam("limit") int limit
    ) {
        return tradeAggregationService.latest(limit);
    }

    @GetMapping(value = "/stream-v2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TradesAggregation>> streamTrades(
            @RequestParam(value = "symbols", required = false) List<String> symbols
    ) {
        Flux<TradesAggregation> flux = symbols == null || symbols.isEmpty() ? tradeAggregationService.watchAll() : tradeAggregationService.watchSymbols(symbols);
        return flux.map(tradesAggregation -> {
            log.info("Sending event: {}", tradesAggregation.getId());
            return ServerSentEvent.<TradesAggregation>builder()
                    .event("trades")
                    .data(tradesAggregation)
                    .build();
        });
    }

    @GetMapping("today-analytics")
    public List<BatchTradesAggregation> getTodayAnalytics() {
        return batchTradesAggregationService.getTodayTrades();
    }


}
