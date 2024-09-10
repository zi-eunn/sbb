package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionService questionService;
//	@Autowired
//	private QuestionRepository questionRepository;
//
//	@Autowired
//	private AnswerRepository answerRepository;
//
//	@Transactional
	@Test
	void testJpa() {
//		Question q1 = new Question();
//		q1.setSubject("sbb가 무엇인가요?");
//		q1.setContent("sbb에 대해서 알고 싶습니다.");
//		q1.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q1);  // 첫번째 질문 저장
//
//		Question q2 = new Question();
//		q2.setSubject("스프링부트 모델 질문입니다.");
//		q2.setContent("id는 자동으로 생성되나요?");
//		q2.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q2);  // 두번째 질문 저장

		//findAll 메서드
//		List<Question> all = this.questionRepository.findAll();
//		assertEquals(2, all.size());
//
//		Question q = all.get(0);
//		assertEquals("sbb가 무엇인가요?", q.getSubject());

		//findById 메서드
//		Optional<Question> oq = this.questionRepository.findById(5);
//		if(oq.isPresent()) {
//			Question q = oq.get();
//			assertEquals("sbb가 무엇인가요?", q.getSubject());
//			}

		//findBySubject
//		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
//		assertEquals(5, q.getId());

		//findBySubjectAndContent
//		Question q = this.questionRepository.findBySubjectAndContent (
//				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
//		assertEquals(5, q.getId());

		//findBySubjectLike
//		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
//		Question q = qList.get(0);
//		assertEquals("sbb가 무엇인가요?", q.getSubject());

		//질문 엔티티의 데이터를 수정하는 테스트 코드
//		Optional<Question> oq = this.questionRepository.findById(5);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		q.setSubject("수정된 제목");
//		this.questionRepository.save(q); //리포지터리의 save 메서드 사용

		//질문 데이터 삭제
//		assertEquals(2, this.questionRepository.count());
//		Optional<Question> oq = this.questionRepository.findById(5);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		this.questionRepository.delete(q);
//		assertEquals(1, this.questionRepository.count());

		//답변 데이터 저장하기
		//답을 생성하려면 질문이 필요하므로 우선 질문을 조회함
//		Optional<Question> oq = this.questionRepository.findById(6);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//
//		Answer a = new Answer();
//		a.setContent("네 자동으로 생성됩니다.");
//		a.setQuestion(q); //어떤 질문의 답변인지 알기위해서 Question 객체가 필요.
//		a.setCreateDate(LocalDateTime.now());
//		this.answerRepository.save(a);

		//답변 데이터 조회하기
		//답변 엔티티도 질문 엔티티와 마찬가지로 id 속성이 기본키 -> 자동 생성
//		Optional<Answer> oa = this.answerRepository.findById(1);
//		assertTrue(oa.isPresent());
//		Answer a = oa.get();
//		//조회한 답변과 연결된 질문의 id가 6인지 조회
//		assertEquals(6, a.getQuestion().getId());

		//답변 데이터를 통해 질문 데이터를 찾기 vs 질문 데이터를 통해 답변 데이터 찾기
		//답변에 연결된 질문 -> a.getQuestion()
		//질문 데이터에서 답변 데이터 찾기
//		Optional<Question> oq = this.questionRepository.findById(6);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
		/*QuestionRepository가 findById 메서드를 통해 Question 객체를 조회하고 나면 DB 세션이 끊어짐
		따라서 이후에 실행되는 q.getAnswerList() 메서드는 세션이 종료되어 오류가 발생함.
		answerList는 q객체를 조회할때가 아니라 q.getAnswerList() 메서드를 호출하는 시점에 가져오기 때문!
		-> 지연(Lazy) : 데이터를 필요한 시점에 가져오는 방식
		<-> q 객체를 조회할때 answer 리스트를 미리 모두 가져오는 방식 : 즉시(Eager)
		@OneToMany, @ManyToOne 애너테이션의 옵션으로 fetch=FetchType.LAZY 또는 fetch=FetchType.EAGER 사용가능
		*
		위 문제는 테스트 코드에서만 발생, @Transactional 애너테이션을 사용하면 해결됨*/

//		List<Answer> answerList = q.getAnswerList(); //객체로부터 answer 리스트를 구하는 메서드
//		//답변 데이터의 개수는 1개
//		assertEquals(1, answerList.size());
//		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());

		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content, null);
		}
	}

}
