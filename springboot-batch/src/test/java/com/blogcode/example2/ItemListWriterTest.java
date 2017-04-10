package com.blogcode.example2;

import com.blogcode.example2.batch.ItemListJobConfiguration;
import com.blogcode.example2.domain.Sales;
import com.blogcode.example2.domain.SalesRepository;
import com.blogcode.example2.domain.TaxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"job.name=" + ItemListJobConfiguration.JOB_NAME})
public class ItemListWriterTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private TaxRepository taxRepository;

    @Test
    public void processor에서_writer로_list를_넘긴다() throws Exception {
        //given
        salesRepository.save(Arrays.asList(
                new Sales(10000L, 1L),
                new Sales(20000L, 2L),
                new Sales(30000L, 3L)));

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(jobExecution.getStatus(), is(org.springframework.batch.core.BatchStatus.COMPLETED));
        assertThat(taxRepository.findAll().size(), is(9));
    }
}
