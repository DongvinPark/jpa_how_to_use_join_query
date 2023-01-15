package com.example.jpa_test.persist;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "wedul_classes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WedulClasses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wedulClassesId;

    @OneToMany(mappedBy = "wedulClasses", fetch = FetchType.LAZY)
    private Set<WedulStudent> wedulStudentList = new LinkedHashSet<>();

    private String classesName;

    private String classesAddr;

}











