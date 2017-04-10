package com.blogcode.example1.batch;

import com.blogcode.example1.domain.Person;
import com.blogcode.example1.domain.PersonCopy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class PersonCopyItemProcessor implements ItemProcessor<Person, PersonCopy> {
    private static final Logger log = LoggerFactory.getLogger(PersonCopyItemProcessor.class);

    @Override
    public PersonCopy process(Person person) throws Exception {
        final PersonCopy transformedPerson = new PersonCopy(person.getFirstName(), person.getLastName());

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}
