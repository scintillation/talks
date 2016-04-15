package at.scintillation.talks.meimarie.service.impl;

import at.scintillation.talks.meimarie.dto.Transaction;
import at.scintillation.talks.meimarie.service.TransactionSearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.RangeQueryBuilder;
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
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

/**
 * Actual implementation of {@code TransactionSearchService} accessing Elasticsearch.
 *
 * @author <a href='mailto:elisabeth.rosemann@scintillation.at'>Elisabeth Rosemann</a>
 * @since 14.04.2016
 */
@Service
public class TransactionSearchServiceImpl implements TransactionSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * @InheritDoc
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
     * @InheritDoc
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
        return (Range) aggregations.get(posOrNegName);
    }


    /**
     * @InheritDoc
     */
    public DateHistogram getTransactionSumsPerInterval(DateHistogram.Interval interval, boolean isSpending) {

        String transPerMonth = "transactions_per_month";
        DateHistogramBuilder dateHistogramBuilder = AggregationBuilders.dateHistogram(transPerMonth)
                .field("date")
                .interval(interval)
                .subAggregation(AggregationBuilders.sum("sum").field("amount"));

        RangeQueryBuilder query = rangeQuery("amount").from("0");
        if (isSpending) {
            query = rangeQuery("amount").to("0");
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .addAggregation(dateHistogramBuilder)
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        return (DateHistogram) aggregations.get(transPerMonth);
    }


    /**
     * @InheritDoc
     */
    public TermResult getTopTerms() {
        String tagsName = "top_tags";
        FacetRequest facet = new TermFacetRequestBuilder(tagsName).fields("tags").size(5).build();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFacet(facet)
                .build();
        FacetedPage<Transaction> result = elasticsearchTemplate.queryForPage(searchQuery, Transaction.class);
        return (TermResult) result.getFacet(tagsName);
    }

}
