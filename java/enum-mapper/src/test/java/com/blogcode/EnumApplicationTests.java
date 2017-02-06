package com.blogcode;

import com.blogcode.after.EnumMapper;
import com.blogcode.after.EnumValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
