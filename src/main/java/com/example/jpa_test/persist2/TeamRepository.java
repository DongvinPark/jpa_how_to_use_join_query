package com.example.jpa_test.persist2;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findTeamsByName(String name);

    Team findTeamByName(String name);

    Optional<Team> findNullableTeamByName(String name);
}
