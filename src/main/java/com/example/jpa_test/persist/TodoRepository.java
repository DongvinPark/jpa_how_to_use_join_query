package com.example.jpa_test.persist;

import java.util.Collection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    Slice<TodoEntity> findAllByFinishedIsFalseAndAuthorUserPKIdIn(
        PageRequest pageRequest, Collection<Long> authorUserPKId
    );

}
