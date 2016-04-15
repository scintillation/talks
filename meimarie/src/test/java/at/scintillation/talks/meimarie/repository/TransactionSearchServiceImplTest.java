package at.scintillation.talks.meimarie.repository;

import at.scintillation.talks.meimarie.TestConfig;
import at.scintillation.talks.meimarie.dto.Transaction;
import at.scintillation.talks.meimarie.service.impl.TransactionSearchServiceImpl;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
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
public class TransactionSearchServiceImplTest {

    @Autowired
    private ElasticsearchOperations operations;
    @Resource
    private TransactionRepository repo;
    @Autowired
    private TransactionSearchServiceImpl searchService;

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
    public void testGetTransactionSumsPerInterval() {
        TransactionRepositoryTest.addTransactionsToRepo(repo);

        //spendings
        DateHistogram result = searchService.getTransactionSumsPerInterval(DateHistogram.Interval.DAY, true);
        assertThat(result.getBuckets().size(), is(3));
        assertThat(((InternalSum) result.getBuckets().get(0).getAggregations().get("sum")).getValue(), is(-129.11));
        assertThat(((InternalSum) result.getBuckets().get(1).getAggregations().get("sum")).getValue(), is(-21.0));
        assertThat(((InternalSum) result.getBuckets().get(2).getAggregations().get("sum")).getValue(), is(-20.0));

        //withdrawals
        result = searchService.getTransactionSumsPerInterval(DateHistogram.Interval.DAY, false);
        assertThat(result.getBuckets().size(), is(3));
        assertThat(((InternalSum) result.getBuckets().get(0).getAggregations().get("sum")).getValue(), is(13.21));
        assertThat(((InternalSum) result.getBuckets().get(1).getAggregations().get("sum")).getValue(), is(12.0));
        assertThat(((InternalSum) result.getBuckets().get(2).getAggregations().get("sum")).getValue(), is(21.0));
    }


    @Test
    public void testGetTopTerms() {
        TransactionRepositoryTest.addTransactionsToRepo(repo);

        TermResult result = searchService.getTopTerms();
        assertThat(result.getTerms().size(), is(5));
        assertThat(result.getTerms().get(1).getTerm(), is("alex"));
        assertThat(result.getTerms().get(1).getCount(), is(3));
        assertThat(result.getTerms().get(2).getTerm(), is("sth"));
        assertThat(result.getTerms().get(2).getCount(), is(2));
    }


}
