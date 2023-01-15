package com.example.jpa_test.persist;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WedulClassesRepository extends JpaRepository<WedulClasses, Long> {

    @Query(value = "select DISTINCT c from WedulClasses c left join fetch c.wedulStudentList")
    List<WedulClasses> findAllWithStudent();

}
