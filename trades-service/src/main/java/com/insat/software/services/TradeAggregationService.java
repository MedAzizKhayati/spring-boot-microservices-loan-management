package com.insat.software.services;

import com.insat.software.models.TradesAggregation;
import com.insat.software.repositories.TradeAggregationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import com.mongodb.client.model.changestream.OperationType;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TradeAggregationService {
    private ReactiveMongoTemplate reactiveMongoTemplate;
    private TradeAggregationRepository tradeAggregationRepository;

    public List<String> getSymbols() {
        return tradeAggregationRepository.findAllSymbols().collectList().block();
    }


    public Flux<TradesAggregation> findTrades(List<String> symbols) {
        // Where id starts with symbol
        return tradeAggregationRepository.findAllByIdStartingWith(symbols);
    }

    // Watch for changes in the database
    public Flux<TradesAggregation> watchAll() {
        return reactiveMongoTemplate.changeStream(TradesAggregation.class)
                .listen()
                .filter(changeEvent -> Objects.equals(changeEvent.getOperationType(), OperationType.INSERT))
                .mapNotNull(ChangeStreamEvent::getBody);
    }

    public Flux<TradesAggregation> watchSymbols(List<String> symbols) {
        return reactiveMongoTemplate.changeStream(TradesAggregation.class)
                .listen()
                .filter(changeEvent -> Objects.equals(changeEvent.getOperationType(), OperationType.INSERT))
                .mapNotNull(ChangeStreamEvent::getBody)
                .filter(tradesAggregation -> symbols.contains(tradesAggregation.getSymbol()));
    }

    public Mono<Page<TradesAggregation>> latest(int size) {
        Pageable pageable = PageRequest.of(0, size);
        Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "$natural"));
        Query countQuery = Query.of(query).limit(0).skip(0);
        Mono<Long> countMono = reactiveMongoTemplate.count(countQuery, TradesAggregation.class);
        Mono<List<TradesAggregation>> resultsMono = reactiveMongoTemplate
                .find(query, TradesAggregation.class)
                .collectList();
        return Mono.zip(countMono, resultsMono)
                .map(tuple -> PageableExecutionUtils.getPage(tuple.getT2(), pageable, tuple::getT1));
    }
}
