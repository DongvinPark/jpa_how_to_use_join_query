package com.example.jpa_test.persist;

import com.example.jpa_test.persist2.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WedulStudentRepository extends JpaRepository<WedulStudent, Long> {

    @Query("select m from WedulStudent m left join fetch m.wedulClasses where m.wedulClasses.wedulClassesId = :id") // (1)
    List<WedulStudent> findAllWedulStudents(Long id);

}
