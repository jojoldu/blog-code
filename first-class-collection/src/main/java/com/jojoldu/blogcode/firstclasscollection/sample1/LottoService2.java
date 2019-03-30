package com.jojoldu.blogcode.firstclasscollection.sample1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LottoService2 {

    public void createLottoNumber() {
        LottoTicket lottoTicket = new LottoTicket(createNonDuplicateNumbers());

        //이후 로직 쭉쭉 실행
    }


    private List<Long> createNonDuplicateNumbers() {
        return new ArrayList<>();
    }
}
