package at.scintillation.talks.meimarie.service;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.springframework.data.elasticsearch.core.facet.result.StatisticalResult;
import org.springframework.data.elasticsearch.core.facet.result.TermResult;

/**
 * Elasticsearch specific searches on the data store.
 *
 * @author <a href='mailto:elisabeth.rosemann@scintillation.at'>Elisabeth Rosemann</a>
 * @since 15.04.2016
 */
public interface TransactionSearchService {

    //@formatter:off
    /**
     * Retrieves the statistical descriptive analysis for the overall data (max, min, mean, median, percentiles, etc)
     * <pre>{
    "query": {
        "match_all": {}
    },
    "aggs" : {
        "amount_stats" : { "stats" : { "field" : "amount" } }
    }
}</pre>
     */
    //@formatter:on
    StatisticalResult getAmountDescriptiveAnalysis();

    //@formatter:off
    /**
     * Retrieves the total amount for the given date interval (e.g. sums per day, month or year). For bar charting.
     * <pre>{
    "aggs": {
        "transactions_per_month": {
            "date_histogram": {
                "field": "date",
                "interval": "month"
            },
            "aggs": {
                "sum_amount": {
                    "sum": {
                        "field": "amount"
                    }
                }
            }
        }
    }
}</pre>
     */
    //@formatter:offon
    DateHistogram getTransactionSumsPerInterval(DateHistogram.Interval interval, boolean isSpending);

    //@formatter:off
    /**
     * Retrieves the top 5 terms used.
     * <pre>{
    "aggs": {
        "top-tags": {
            "terms": {
                "field": "tags",
                "size": 5
            }
        }
    }
}</pre>
     */
    //@formatter:on
    TermResult getTopTerms();
}
