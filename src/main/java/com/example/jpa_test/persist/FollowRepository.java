package com.example.jpa_test.persist;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    List<FollowEntity> findAllByFollowSentUserPKId(Long followSentUserPKId);

    Optional<FollowEntity> findByFollowSentUserPKIdEqualsAndFollowTargetUserPKIdEquals(Long followSentUserPKId,
        Long followTargetUserPKId);

}
