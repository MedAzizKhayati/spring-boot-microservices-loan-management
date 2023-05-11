package com.insat.software.repositories;

import com.insat.software.models.TradesAggregation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TradeAggregationRepository extends ReactiveMongoRepository<TradesAggregation, String> {
    @Query("{ 'symbol' : { $in: ?0 } }")
    Flux<TradesAggregation> findAllByIdStartingWith(List<String> symbols);

    @Aggregation("{ $group: { _id: '$symbol' } }")
    Flux<String> findAllSymbols();

    // Paginate all latest first w

}
