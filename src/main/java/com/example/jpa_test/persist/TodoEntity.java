package com.example.jpa_test.persist;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorUserPKId;

    private boolean finished;

    //-------- SETTING FOR JOIN QUERY --------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_target_userpkid")
    private FollowEntity followEntity;

}
