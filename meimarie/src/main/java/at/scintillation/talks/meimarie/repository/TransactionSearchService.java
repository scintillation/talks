package at.scintillation.talks.meimarie.repository;

import at.scintillation.talks.meimarie.dto.Transaction;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.facet.FacetRequest;
import org.springframework.data.elasticsearch.core.facet.request.StatisticalFacetRequestBuilder;
import org.springframework.data.elasticsearch.core.facet.request.TermFacetRequestBuilder;
import org.springframework.data.elasticsearch.core.facet.result.StatisticalResult;
import org.springframework.data.elasticsearch.core.facet.result.TermResult;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * @author <a href='mailto:elisabeth.rosemann@scintillation.at'>Elisabeth Rosemann</a>
 * @since 14.04.2016
 */
@Service
public class TransactionSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ElasticsearchClient elasticSearchClient;

    /**
     * <pre>{
    "query": {
        "match_all": {}
    },
    "aggs" : {
        "amount_stats" : { "stats" : { "field" : "amount" } }
    }
}</pre>
     */
    public StatisticalResult getAmountDescriptiveAnalysis() {
        String statsName = "amount_stats";
        FacetRequest stats = new StatisticalFacetRequestBuilder(statsName).field("amount").build();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFacet(stats)
                .build();
        FacetedPage<Transaction> result = elasticsearchTemplate.queryForPage(searchQuery, Transaction.class);
        return (StatisticalResult) result.getFacet(statsName);
    }

    /**
     * <pre>{
    "query": {
        "match_all": {}
    },
    "aggs" : {
        "pos_and_neg" : {
            "range" : {
                "field" : "amount",
                "keyed" : true,
                "ranges" : [
                    { "key" : "spending", "to" : 0 },
                    { "key" : "withdrawal", "from" : 0 }
                ]
            },
            "aggs" : {
                "amount_stats" : { "stats" : { "field" : "amount" } }
            }
        }
    }
}</pre>
     */
    public Range getDescriptiveAnalysisForSpendingAndWithdrawals() {
        String posOrNegName = "pos_or_neg";
        RangeBuilder rangeBuilder = AggregationBuilders.range(posOrNegName)
                .field("amount")
                .addUnboundedFrom("withdrawal", 0)
                .addUnboundedTo("spending", 0)
                .subAggregation(AggregationBuilders.avg("avg").field("amount"))
                .subAggregation(AggregationBuilders.max("max").field("amount"))
                .subAggregation(AggregationBuilders.min("min").field("amount"))
                .subAggregation(AggregationBuilders.count("count").field("amount"))
                .subAggregation(AggregationBuilders.percentiles("percentiles").field("amount"));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withIndices("meimarie")
                .addAggregation(rangeBuilder)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        return (Range)aggregations.get(posOrNegName);
    }

    public DateHistogram getTransactionSumsPerMonth() {
        return getTransactionSumsPer(DateHistogram.Interval.MONTH);
    }

    public DateHistogram getTransactionSumsPerYear() {
        return getTransactionSumsPer(DateHistogram.Interval.YEAR);
    }

    public DateHistogram getTransactionSumsPerDay() {
        return getTransactionSumsPer(DateHistogram.Interval.DAY);
    }

    /**
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
    private DateHistogram getTransactionSumsPer(DateHistogram.Interval interval) {
        String transPerMonth = "transactions_per_month";
        DateHistogramBuilder dateHistogramBuilder = AggregationBuilders.dateHistogram(transPerMonth)
                .field("date")
                .interval(interval)
                .subAggregation(AggregationBuilders.sum("sum").field("amount"));

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .addAggregation(dateHistogramBuilder)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        return (DateHistogram)aggregations.get(transPerMonth);
    }

    /**
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
    public TermResult getTopTerms() {
        String tagsName = "top_tags";
        FacetRequest facet = new TermFacetRequestBuilder(tagsName).fields("tags").size(10).build();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFacet(facet)
                .build();
        FacetedPage<Transaction> result = elasticsearchTemplate.queryForPage(searchQuery, Transaction.class);
        return (TermResult) result.getFacet(tagsName);
    }

}
