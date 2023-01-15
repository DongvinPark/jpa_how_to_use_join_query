package com.example.jpa_test.persist;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    Slice<TodoEntity> findAllByFinishedIsFalseAndAuthorUserPKIdIn(
        PageRequest pageRequest, Collection<Long> authorUserPKId
    );

    @Query("select distinct m from TodoEntity m left join fetch m.followEntity where m.finished = false and m.followEntity.followSentUserPKId = :followSendUserPKId")
    Slice<TodoEntity> findAllTodoEntitys(PageRequest pageRequest, Long followSendUserPKId);

    @Query("select m from WedulStudent m left join fetch m.wedulClasses where m.wedulClasses.wedulClassesId = :id") // (1)
    List<WedulStudent> findAllWedulStudents(Long id);

}
