package com.insat.software.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("trades_aggregations")
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradesAggregation {

    @Getter
    public static class Volume {
        private double total;
        private double min;
        private double max;
        private double avg;
    }

    @Getter
    public static class Price {
        private double total;
        private double min;
        private double max;
        private double avg;
    }

    @Getter
    public static class Timestamp {
        private long first;
        private long last;
        private long avg;
    }


    @Id
    private String id;
    private Volume volume;
    private Price price;
    private Timestamp timestamp;
    private String symbol;
    private long createdAt;
    private int numberOfTrades;
}
