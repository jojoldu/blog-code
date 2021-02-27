package com.jojoldu.blogcode.entityql.sql.bulkinsert.academy;

import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Getter
public class AcademyUniqueMatcher {

    private final Map<String, Academy> map = new LinkedHashMap<>();

    public AcademyUniqueMatcher(List<Academy> items) {
        for (Academy academy : items) {
            String collectorMatchKey = UUID.randomUUID().toString();
            academy.setMatchKey(collectorMatchKey);
            map.put(collectorMatchKey, academy);
        }
    }

    public List<Academy> getAcademies() {
        return new ArrayList<>(map.values());
    }

    public Optional<Academy> get(String collectorMatchKey) {
        return Optional.ofNullable(map.get(collectorMatchKey));
    }
}
