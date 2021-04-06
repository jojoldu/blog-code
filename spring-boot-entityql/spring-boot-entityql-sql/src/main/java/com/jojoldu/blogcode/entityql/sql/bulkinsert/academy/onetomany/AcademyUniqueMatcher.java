package com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.onetomany;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.student.Student;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


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

    public List<Student> get(Academy idAcademy, LocalDateTime now) {
        Academy academy = map.get(idAcademy.getMatchKey());

        if(academy == null) {
            return new ArrayList<>();
        }

        return academy.getStudents().stream()
                .map(student -> student.setBulkInsert(idAcademy, now))
                .collect(Collectors.toList());
    }
}
