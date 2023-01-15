package com.example.jpa_test;

import com.example.jpa_test.persist.FollowEntity;
import com.example.jpa_test.persist.FollowRepository;
import com.example.jpa_test.persist.TodoEntity;
import com.example.jpa_test.persist.TodoRepository;
import com.example.jpa_test.persist.WedulClasses;
import com.example.jpa_test.persist.WedulClassesRepository;
import com.example.jpa_test.persist.WedulStudent;
import com.example.jpa_test.persist.WedulStudentRepository;
import com.example.jpa_test.persist2.Member;
import com.example.jpa_test.persist2.MemberRepository;
import com.example.jpa_test.persist2.Team;
import com.example.jpa_test.persist2.TeamRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.TemplateRenderer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final FollowRepository followRepository;
    private final TodoRepository todoRepository;
    private final WedulClassesRepository wedulClassesRepository;
    private final WedulStudentRepository wedulStudentRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    /**
     * 본 코드는 두 개의 테이블을 조인하여 한 번의 쿼리로 작업을 끝내는 것과,
     * 두 개의 테이블 각각에 대하여 단순 퀴리를 1개 씩 날려서 작업을 끝내는 것 중
     * 무엇이 성능이 더 좋은지를 테스트 하기 위해서 작성되었다.
     * */

    //팔로우 테이블을 초기화 한다. 5001 번 유저가 1~5000번 까지의 유저들을 팔로우 하고 있으며,
    //그 외 나머지 유저 19_999 명은 랜덤한 유저들을 팔로우 하고 있다고 가정한다.


    @GetMapping("/run/{idListLimit}")
    public void run(
        @PathVariable Long idListLimit
    ){

/*



        //MariaDB에 성공적으로 초기화를 마쳤으므로, ddl 설정을 none으로 바꾸고 초기화 코드도 전부 주석처리한다.

        //5001번 유저의 팔로우 정보는 1~5000 번의 주키 값들 중에서 랜덤하게 1000개를 선택하도록 한다.
        List<Long> followPKIdxList = new ArrayList<>();
        initList(followPKIdxList);

        Set<Long> randomIdxSet = new HashSet<>();
        for(int i=0; i<1000; i++){
            randomIdxSet.add(followPKIdxList.get(i));
        }

        long targetIdx = 1;

        //팔로우 테이블에 5천 개의 팔로우 관계 튜플을 넣는다.
        //주키는 직접 매핑한다.
        //1001번 유저는 1~1000 번의 유저들을 팔로우하고 있는 것이다.
        for(long i=1; i<=5000; i++){
            if(randomIdxSet.contains(i)){
                followRepository.save(
                    FollowEntity.builder()
                        .id(i)
                        .followSentUserPKId(1001L)
                        .followTargetUserPKId(targetIdx)
                        .build()
                );
                targetIdx++;
            }//if
            else{
                followRepository.save(
                    FollowEntity.builder()
                        .id(i)
                        .followSentUserPKId(0L)
                        .followTargetUserPKId(0L)
                        .build()
                );
            }
        }



        //투두 테이블을 셋팅한다.
        //팔로우 엔티티에 매핑된 두 개의 투두 아이템을 만드는 작업을 1000번 반복한다.
        //
        for(long j=1; j<=1000; j++) {
                FollowEntity followRelationEntity = followRepository.findByFollowSentUserPKIdEqualsAndFollowTargetUserPKIdEquals(
                        1001L, j)
                    .orElseThrow(() -> new RuntimeException("follow entity not found"));

                //트루인 경우를 저장한다.
                todoRepository.save(
                    TodoEntity.builder()
                        .authorUserPKId(j)
                        .followEntity(followRelationEntity)
                        .finished(true)
                        .build()
                );

                //펄스인 경우를 저장한다.
                todoRepository.save(
                    TodoEntity.builder()
                        .authorUserPKId(j)
                        .followEntity(followRelationEntity)
                        .finished(false)
                        .build()
                );
        }// 1000 for


        //나머지 4000개의 튜플을 아무 의미 없는 값으로 집어 넣는다.
        for(long k=1; k<=4000; k++){
            todoRepository.save(
                TodoEntity.builder()
                    .authorUserPKId(0L)
                    .followEntity(null)
                    .finished(false)
                    .build()
            );
        }

*/



        //쿼리 실행 시간을 측정한다.


        //두 번째는 조인 쿼리다.
        //DB 입출력은 1번만 발생하지만, N+1 문제를 신경써야 한다.
        Long start2 = System.currentTimeMillis();
        Slice<TodoEntity> secondQueryResult = todoRepository.findAllTodoEntitys(PageRequest.of(0,30), 1001L);
        Long end2 = System.currentTimeMillis();

        System.out.println("조인 쿼리 1개 : " + (end2-start2) + " 밀리 초");
        System.out.println("secondQueryResult.getContent().size() = " + secondQueryResult.getContent().size());

        for(TodoEntity entity : secondQueryResult){
            System.out.println(entity.getAuthorUserPKId() + "/" + entity.isFinished());
        }


        //첫 번째는 단순 쿼리 2 개다.
        //5001 번 유저가 팔로우 하고 있는 다른 유저들 5천 명(1~5000번 유저)을 찾은 다음,
        //투두 엔티티에서 1~5000번 유저들이 올린 투두 들 중에서 finished 가 false인 것만 페이징 처리하여 골라내는 것이다.
        //DB 입출력이 2번 발생하지만 N+1문제는 절대 발상하지 않음을 보장한다.

        /*List<Long> cachedIdList = new ArrayList<>();
        for(long i=1; i<=idListLimit; i++){
            cachedIdList.add(i);
        }

        Collections.shuffle(cachedIdList);*/

        Long start1 = System.currentTimeMillis();
        //특정 유저가 팔로우한 다른 유저들의 주키 아이디 번호를 알아내기 위한 첫 번째 단순 쿼리
        List<Long> idList = followRepository.findAllByFollowSentUserPKId(1001L).stream().map(FollowEntity::getFollowTargetUserPKId).collect(
            Collectors.toList());

        //두 번째 단순 쿼리. 첫 번째 단쉰 쿼리의 결과물은 캐시된 것을 건네는 것으로 대체한다.
        List<TodoEntity> todoEntities = todoRepository.findAllByFinishedIsFalseAndAuthorUserPKIdIn(PageRequest.of(0, 30), idList).getContent();
        Long end1 = System.currentTimeMillis();

        System.out.println("단순 쿼리 2개 : " + (end1-start1) + " 밀리 초");
        System.out.println("idList.size() = " + idList.size());
        System.out.println("todoEntities size() = " + todoEntities.size());

        //최종 결과물 출력. 작성자 아이디는 1~30 이렇게 총 30개가 나와야 하고, 각각의 내용물을 전부 false여야 한다.
        for(TodoEntity todoEntity : todoEntities){
            System.out.println(todoEntity.getAuthorUserPKId() + "/" + todoEntity.isFinished());
        }

    }//end of run func








    //----------- PRIVATE HELPER METHODS -----------

    @GetMapping("join-run")
    public void joinTest(){


        //클래스 테이블 초기화
        wedulClassesRepository.save(
            WedulClasses.builder()
                .wedulClassesId(3L)
                .classesName("위메프")
                .classesAddr("삼성")
                .build()
        );

        wedulClassesRepository.save(
            WedulClasses.builder()
                .wedulClassesId(4L)
                .classesName("피앤피시큐어")
                .classesAddr("판교")
                .build()
        );

        wedulClassesRepository.save(
            WedulClasses.builder()
                .wedulClassesId(5L)
                .classesName("배달의민족")
                .classesAddr("잠실")
                .build()
        );

        WedulClasses classOne = wedulClassesRepository.findById(1L).orElseThrow(()->new RuntimeException("not found"));
        WedulClasses classTwo = wedulClassesRepository.findById(2L).orElseThrow(()->new RuntimeException("not found"));
        WedulClasses classThree = wedulClassesRepository.findById(3L).orElseThrow(()->new RuntimeException("not found"));

        //스튜던트 테이블 초기화
        wedulStudentRepository.save(
            WedulStudent.builder()
                .studentName("wedul")
                .wedulClasses(classThree)
                .studentAge(33)
                .studentType(StudentType.BEGINNER)
                .build()
        );

        wedulStudentRepository.save(
            WedulStudent.builder()
                .studentName("dani")
                .wedulClasses(classThree)
                .studentAge(11)
                .studentType(StudentType.STUDENT)
                .build()
        );

        wedulStudentRepository.save(
            WedulStudent.builder()
                .studentName("chori")
                .wedulClasses(classOne)
                .studentAge(214)
                .studentType(StudentType.GRADUATE)
                .build()
        );

        wedulStudentRepository.save(
            WedulStudent.builder()
                .studentName("추가")
                .wedulClasses(classTwo)
                .studentAge(124)
                .studentType(StudentType.STUDENT)
                .build()
        );

        wedulStudentRepository.save(
            WedulStudent.builder()
                .studentName("db")
                .wedulClasses(classTwo)
                .studentAge(12)
                .studentType(StudentType.STUDENT)
                .build()
        );


        //조인 쿼리 결과물 생성
        //List<WedulClasses> result = wedulClassesRepository.findAllWithStudent();
        List<WedulStudent> result = wedulStudentRepository.findAllWedulStudents(2L);

        //조인 쿼리 결과물 출력
        for(WedulStudent wedulStudent : result){
                System.out.println(wedulStudent.getStudentName());
        }
    }//end of join-run func





    @GetMapping("/join-run2")
    public void joinTestSecond(){
        Team barcelonaFc = Team.builder()
            .name("Barcelona FC")
            .build();
        Team realMadridCf = Team.builder()
            .name("Real Madrid CF")
            .build();
        teamRepository.save(barcelonaFc);
        teamRepository.save(realMadridCf);


        Member lionelMessi = Member.builder()
            .age(34)
            .team(barcelonaFc)
            .username("Lionel Messi")
            .build();
        Member karimBenzema = Member.builder()
            .age(33)
            .team(realMadridCf)
            .username("Karim Benzema")
            .build();
        memberRepository.save(lionelMessi);
        memberRepository.save(karimBenzema);


        // when
        List<Member> members = memberRepository.findAllMembers();
        members.forEach(m -> {
            System.out.println(m);
            System.out.println(m.getTeam());
        });
    }





    private void initList(List<Long> list){
        for(long i=1; i<=5_000; i++){
            list.add(i);
        }
        Collections.shuffle(list);
    }//func

}























