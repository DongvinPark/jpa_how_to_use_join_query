package com.example.jpa_test.persist;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    List<FollowEntity> findAllByFollowSentUserPKId(Long followSentUserPKId);

    @Query(value = "select c  from FollowEntity c left join fetch c.todoEntityList")
    Slice<FollowEntity> findAllWithTodoEntity(PageRequest pageRequest);

}
