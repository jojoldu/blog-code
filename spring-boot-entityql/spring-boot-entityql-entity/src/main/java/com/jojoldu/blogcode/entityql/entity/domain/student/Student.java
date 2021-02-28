package com.jojoldu.blogcode.entityql.entity.domain.student;

/**
 * Created by jojoldu@gmail.com on 2019-01-11
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

import com.jojoldu.blogcode.entityql.entity.domain.BaseTimeEntity;
import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "student")
public class Student extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "academy_no")
    private Integer academyNo;

    @ManyToOne
    @JoinColumn(name = "academy_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_student_academy"))
    private Academy academy;

    @Builder
    public Student(String name, int academyNo) {
        this.name = name;
        this.academyNo = academyNo;
    }

    public Student(String name, Integer academyNo, Academy academy) {
        this.name = name;
        this.academyNo = academyNo;
        this.academy = academy;
    }

    public void setAcademy(Academy academy) {
        this.academy = academy;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public Student setBulkInsert (Academy academy, LocalDateTime now) {
        setAcademy(academy);
        setCurrentTime(now);

        return this;
    }
}
