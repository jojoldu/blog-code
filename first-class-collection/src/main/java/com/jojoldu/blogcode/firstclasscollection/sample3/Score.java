package com.jojoldu.blogcode.firstclasscollection.sample3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Score {

    private final String teamA;
    private final String teamB;
    private final long teamAScore;
    private final long teamBScore;


}
