package com.insat.software.repositories;

import com.insat.software.models.BatchTradesAggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BatchTradesAggregationRepository extends ReactiveMongoRepository<BatchTradesAggregation, String> {
    @Query("{ 'created_at' : { $gt: ?0, $lt: ?1 } }")
    Flux<BatchTradesAggregation> findAllByCreatedAtBetween(long start, long end);
}
