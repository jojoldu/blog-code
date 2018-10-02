package com.jojoldu.blogcode.jpaquerydsl;

import com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.Child;
import com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.Family;
import com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.Parent;
import com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.ParentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2018. 10. 2.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParentRepositoryTest {

    @Autowired
    ParentRepository parentRepository;

    @Test
    public void QuerydslLeftJoin() {
        //given
        for(int i=0;i<10;i++) {
            Parent parent = new Parent("parent"+i);
            if(i % 2 == 0){
                Child child = new Child("child"+i);
                parent.addChild(child);
            }

            parentRepository.save(parent);
        }

        //when
        List<Family> familyList = parentRepository.findFamily();

        //then
        assertThat(familyList.size(), is(10));
    }
}
