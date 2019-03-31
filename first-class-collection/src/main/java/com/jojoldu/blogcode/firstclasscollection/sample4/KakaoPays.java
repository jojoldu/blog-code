package com.jojoldu.blogcode.firstclasscollection.sample4;

import com.jojoldu.blogcode.firstclasscollection.sample3.Pay;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class KakaoPays {

    private final List<Pay> pays;

}
