package at.scintillation.talks.meimarie.repository;

import at.scintillation.talks.meimarie.TestConfig;
import at.scintillation.talks.meimarie.dto.Transaction;
import at.scintillation.talks.meimarie.dto.TransactionType;
import org.elasticsearch.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author <a href='mailto:elisabeth.rosemann@scintillation.at'>Elisabeth Rosemann</a>
 * @since 10.04.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfig.class)
public class TransactionRepositoryTest {


    @Autowired
    private ElasticsearchOperations operations;
    @Resource
    private TransactionRepository repo;

    @Before
    public void emptyData() {
        operations.deleteIndex(Transaction.class);
        operations.createIndex(Transaction.class);
        operations.refresh(Transaction.class, true);
    }

    @Test
    public void shouldIndexSingleEntity() {
        //save one
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(), 13.21, TransactionType.CASH, Arrays.asList("#win", "#poker"));
        repo.save(transaction);
        assertThat(repo.findAll(), is(notNullValue()));
        assertThat(repo.findAll().iterator().next().getAmount(), is(transaction.getAmount()));
    }

    @Test
    public void shouldBulkIndexMultipleEntities() {
        //save more
        Transaction t1 = new Transaction(UUID.randomUUID().toString(), new Date(), 13.21, TransactionType.CASH, Arrays.asList("#win", "#poker"));
        Transaction t2 = new Transaction(UUID.randomUUID().toString(), new Date(), -1.11, TransactionType.CASH, Arrays.asList("#post"));
        Transaction t3 = new Transaction(UUID.randomUUID().toString(), new Date(), -128.0, TransactionType.WIRE_TRANSFER, Arrays.asList("#shoes", "#alex"));
        repo.save(Arrays.asList(t1, t2, t3));

        //lets try to search same records in elasticsearch
        assertThat(repo.findOne(t1.getId()).getId(), is(t1.getId()));
        //count
        assertThat(repo.count(), is(equalTo(3L)));
        assertThat(Iterators.size(repo.findAll().iterator()), is(3));

        //page request which will give first 10 document
        Page<Transaction> page = repo.findAll(new PageRequest(0, 10));
        assertThat(page.getTotalElements(), is(3L));
        page = repo.findAll(new PageRequest(0, 2));
        assertThat(page.getTotalElements(), is(3L));

        // to get all records as DESC on datefield
        Iterable<Transaction> listSorted = repo.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "date")));
        Iterator<Transaction> iterator = listSorted.iterator();
        assertThat(Iterators.size(iterator), is(3));
        if (iterator.hasNext())
            assertThat(iterator.next().getDate(), is(t3.getDate()));
        if (iterator.hasNext())
            assertThat(iterator.next().getDate(), is(t2.getDate()));
        if (iterator.hasNext())
            assertThat(iterator.next().getDate(), is(t1.getDate()));

        //to check whether document exists or not
        assertThat(repo.exists(t1.getId()), is(true));
        assertThat(repo.exists(UUID.randomUUID().toString()), is(false));

        //delete a document by entity
        repo.delete(t1);
        assertThat(repo.count(), is(equalTo(2L)));

        //delete all document
        repo.deleteAll();
        assertThat(repo.count(), is(equalTo(0L)));
    }


    protected static void addTransactionsToRepo(TransactionRepository repo) {
        Calendar c = new GregorianCalendar();
        c.set(113 + 1900, Calendar.OCTOBER, 31);
        Transaction t1 = new Transaction(UUID.randomUUID().toString(), c.getTime(), 13.21, TransactionType.CASH, Arrays.asList("#win", "#poker"));
        Transaction t2 = new Transaction(UUID.randomUUID().toString(), c.getTime(), -1.11, TransactionType.CASH, Arrays.asList("#post"));
        Transaction t3 = new Transaction(UUID.randomUUID().toString(), c.getTime(), -128.0, TransactionType.WIRE_TRANSFER, Arrays.asList("#shoes", "#alex"));
        c.set(115 + 1900, Calendar.OCTOBER, 31);
        Transaction t4 = new Transaction(UUID.randomUUID().toString(), c.getTime(), -21.0, TransactionType.CREDIT_CARD, Arrays.asList("#shoes", "#alex"));
        Transaction t5 = new Transaction(UUID.randomUUID().toString(), c.getTime(), 12.0, TransactionType.WIRE_TRANSFER, Arrays.asList("#sth"));
        c.set(115 + 1900, Calendar.NOVEMBER, 27);
        Transaction t6 = new Transaction(UUID.randomUUID().toString(), c.getTime(), -20.0, TransactionType.WIRE_TRANSFER, Arrays.asList("#shoes", "#alex"));
        Transaction t7 = new Transaction(UUID.randomUUID().toString(), c.getTime(), 21.0, TransactionType.WIRE_TRANSFER, Arrays.asList("#sth"));
        repo.save(Arrays.asList(t1, t2, t3, t4, t5, t6, t7));
    }

}
