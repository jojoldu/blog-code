package com.jojoldu.blogcode.firstclasscollection;

import com.jojoldu.blogcode.firstclasscollection.sample3.Pay;
import com.jojoldu.blogcode.firstclasscollection.sample4.KakaoPays;
import com.jojoldu.blogcode.firstclasscollection.sample4.NaverPays;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamedCollectionTest {

    @Test
    public void 컬렉션을_변수명으로() {
        //given
        List<Pay> naverPays = createNaverPays();
        List<Pay> kakaoPays = createKakaoPays();

        //when

        //then
    }

    @Test
    public void 일급컬렉션의_이름으로() {
        //given
        NaverPays naverPays = new NaverPays(createNaverPays());

        KakaoPays kakaoPays = new KakaoPays(createKakaoPays());

        //when

        //then
    }

    private ArrayList<Pay> createNaverPays() {
        return new ArrayList<>();
    }

    private ArrayList<Pay> createKakaoPays() {
        return new ArrayList<>();
    }
}
