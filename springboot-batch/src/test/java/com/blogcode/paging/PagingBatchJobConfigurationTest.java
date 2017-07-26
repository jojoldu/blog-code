package com.blogcode.paging;

import com.blogcode.paging.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"job.name="+PagingBatchJobConfiguration.JOB_NAME})
public class PagingBatchJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Before
    public void setup() {
        List<Customer> customers = new ArrayList<>();
        for(int i=0; i<100; i++){
            customers.add(customerRepository.save(new Customer("고객"+i)));
        }

        for(int i=0; i<10000000;i++){
            int customerIndex = i%100;
            Customer customer = customers.get(customerIndex);
            shopOrderRepository.save(new ShopOrder(LocalDate.now(), customer));
        }
    }

    @Test
    public void 페이징조회진행시_누락_중복된_데이터가_발생한다() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));

        List<OrderHistory> histories = orderHistoryRepository.findAll();

        Map<Long, Long> counts = histories.stream()
                .collect(Collectors.groupingBy(OrderHistory::getOrderId, Collectors.counting()));

        for (Map.Entry<Long, Long> count: counts.entrySet()) {
            if(count.getValue() > 1){
                System.out.println(count.getKey()+" : count :"+count.getValue());
            }
        }
    }
}
