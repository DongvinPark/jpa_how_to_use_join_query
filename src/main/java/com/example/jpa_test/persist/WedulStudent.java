package com.example.jpa_test.persist;

import com.example.jpa_test.StudentType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Table(name = "wedul_student")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WedulStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wedulStudentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedul_classes_id")
    @JsonBackReference
    private WedulClasses wedulClasses;

    private String studentName;

    private int studentAge;

    @Enumerated(value = EnumType.STRING)
    private StudentType studentType;

}










