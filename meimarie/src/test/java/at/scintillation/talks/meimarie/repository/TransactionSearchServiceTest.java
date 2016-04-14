package at.scintillation.talks.meimarie.repository;

import at.scintillation.talks.meimarie.TestConfig;
import at.scintillation.talks.meimarie.dto.Transaction;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.facet.result.StatisticalResult;
import org.springframework.data.elasticsearch.core.facet.result.TermResult;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author <a href='mailto:elisabeth.rosemann@scintillation.at'>Elisabeth Rosemann</a>
 * @since 14.04.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfig.class)
public class TransactionSearchServiceTest {

    @Autowired
    private ElasticsearchOperations operations;
    @Resource
    private TransactionRepository repo;
    @Autowired
    private TransactionSearchService searchService;

    @Before
    public void emptyData() {
        operations.deleteIndex(Transaction.class);
        operations.createIndex(Transaction.class);
        operations.refresh(Transaction.class, true);
    }

    @Test
    public void testGetDescriptiveAnalysis() {
        TransactionRepositoryTest.addTransactionsToRepo(repo);
        StatisticalResult result = searchService.getAmountDescriptiveAnalysis();
        assertThat(result.getCount(), is(7L));
        assertThat(result.getMin(), is(-128.0));
        assertThat(result.getMax(), is(21.0));
    }

    @Test
    public void testGetDescriptiveAnalysisEnhanced() {
        TransactionRepositoryTest.addTransactionsToRepo(repo);
        Range result = searchService.getDescriptiveAnalysisForSpendingAndWithdrawals();
        assertThat(result.getBuckets().size(), is(2));
        assertThat(((InternalMax)result.getBucketByKey("spending").getAggregations().get("max")).getValue(), is(-1.11));
        assertThat(((InternalMin)result.getBucketByKey("spending").getAggregations().get("min")).getValue(), is(-128.0));
        assertThat(((InternalAvg)result.getBucketByKey("spending").getAggregations().get("avg")).getValue(), is(-42.5275));
        assertThat(((Percentiles)result.getBucketByKey("spending").getAggregations().get("percentiles")).percentile(50), is(-20.5));

        assertThat(((InternalMax)result.getBucketByKey("withdrawal").getAggregations().get("max")).getValue(), is(21.0));
        assertThat(((InternalMin)result.getBucketByKey("withdrawal").getAggregations().get("min")).getValue(), is(12.0));
        assertThat(((InternalAvg)result.getBucketByKey("withdrawal").getAggregations().get("avg")).getValue(), is(15.403333333333334));
    }

    @Test
    public void testGetSumsBy() {
        TransactionRepositoryTest.addTransactionsToRepo(repo);

        DateHistogram result = searchService.getTransactionSumsPerDay();
        assertThat(result.getBuckets().size(), is(3));

        //not working

//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2013-10-31T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(-115.9));
//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2015-10-31T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(-9));
//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2015-11-27T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(1));
//
//        result = searchService.getTransactionSumsPerMonth();
//        assertThat(result.getBuckets().size(), is(3));
//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2013-10-01T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(-1.11));
//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2015-10-01T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(-1.11));
//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2015-11-01T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(-1.11));
//
//        result = searchService.getTransactionSumsPerYear();
//        assertThat(result.getBuckets().size(), is(2));
//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2013-01-01T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(-1.11));
//        assertThat(((InternalSum)result.getBucketByKey(TransactionSearchService.getDateAsLong("2015-01-01T00:00:00.000Z")).getAggregations().get("sum")).getValue(), is(-1.11));
    }


    @Test
    public void testGetTopTerms() {
        TransactionRepositoryTest.addTransactionsToRepo(repo);

        TermResult result = searchService.getTopTerms();
        assertThat(result.getTerms().size(), is(6));
        assertThat(result.getTerms().get(1).getTerm(), is("alex"));
        assertThat(result.getTerms().get(1).getCount(), is(3));
        assertThat(result.getTerms().get(2).getTerm(), is("sth"));
        assertThat(result.getTerms().get(2).getCount(), is(2));
   }


}
