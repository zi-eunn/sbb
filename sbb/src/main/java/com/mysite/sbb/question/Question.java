package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.mysite.sbb.answer.Answer;

import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    /*
    Answer 객체들로 구성된 answerList를 Question 엔티티 속성으로 추가
    @OneToMany 애너테이션 설정
    qeustion.getAnswerList()를 호출하면 질문에서 답변 참조 가능
    * */
    /*
    mappedBy : 참조 엔티티의 속성명 정의.
    즉, Answer 엔티티에서 Question 엔티티를 참조한 속성인 question을 mappedBy에 전달
    */
    /*
    CascadeType.REMOVE : 질문을 삭제하면 답변들도 모두 삭제되도록.
    * */

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;
    //속성값이 서로 중복되지 않게 하기 위해 Set 자료형 사용
}