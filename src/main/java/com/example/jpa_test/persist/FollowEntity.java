package com.example.jpa_test.persist;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowEntity {

    @Id
    private Long id;

    private Long followSentUserPKId;

    private Long followTargetUserPKId;


    //----------- SETTING FOR JOIN QUERY -----------

    @OneToMany(mappedBy = "followEntity", fetch = FetchType.LAZY)
    private List<TodoEntity> todoEntityList;


}
