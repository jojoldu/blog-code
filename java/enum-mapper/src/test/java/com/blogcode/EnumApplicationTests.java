package com.blogcode;

import com.blogcode.after.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by jojoldu@gmail.com on 2017. 2. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnumApplicationTests {

    @Autowired
    private EnumContractRepository enumContractRepository;

    @Test
    public void add() {
        enumContractRepository.save(new EnumContract(
                "우아한짐카",
                1.0,
                EnumContract.CommissionType.MONEY,
                EnumContract.CommissionCutting.ROUND));

        EnumContract saved = enumContractRepository.findOne(1L);

        assertThat(saved.getCommissionType(), is(EnumContract.CommissionType.MONEY));
        assertThat(saved.getCommissionCutting(), is(EnumContract.CommissionCutting.ROUND));
    }


    @Test
    public void EnumModel타입확인() {
        List<EnumModel> enumModels = new ArrayList<>();
        enumModels.add(EnumContract.CommissionType.MONEY);
        enumModels.add(EnumContract.CommissionCutting.CEIL);

        assertThat(enumModels.get(0).getValue(), is("금액"));
        assertThat(enumModels.get(1).getValue(), is("올림"));
    }

    @Autowired
    private EnumMapper enumMapper;

    @Test
    public void get() {
        String key = "commissionType";

        // Controller에 key를 던져 값을 가져왔다고 가정
        Map<String, List<EnumValue>> controllerResult = enumMapper.get(key);

        // JS에서 Controller에서 받은 값에서 원하는 enum type을 가져왔다고 가정
        List<EnumValue> viewResult = controllerResult.get(key);

        EnumValue percent = viewResult.get(0);

        assertThat(percent.getKey(), is("PERCENT"));
        assertThat(percent.getValue(), is("percent"));
    }
}
