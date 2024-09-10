package com.mysite.sbb.question;

import java.security.Principal;
import java.util.List;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

import com.mysite.sbb.answer.AnswerForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
//URL의 프리픽스가 모두 /question으로 시작함.
//프리픽스 -> URL의 접두사 또는 시작 부분을 말함
//@RequestMapping 애너테이션을 통해 메서드 단위에서 /question 생략 가능

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    //@GetMapping("/list")
    //@ResponseBody

//    public String list(Model model) {
//        //findAll 메서드를 사용해 질문 목록 데이터인 questionList를 생성
//        //Model 객체에 questionList라는 이름으로 저장
//        //Model객체 : 자바 클래스와 템플릿간의 연결고리
//        //List<Question> questionList = this.questionRepository.findAll();
//
//        List<Question> questionList = this.questionService.getList();
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        //변하는 id값을 얻을때는 PathVariable 애너테이션 사용

        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
        //템플릿을 리턴하는 것. String이 아님 주의
    }

    @PreAuthorize("isAuthenticated()")
    //위의 애너테이션이 붙은 메서드는 로그인한 경우에만 실행됨.
    //따라서 로그아웃 상태에서 호출되면 로그인 페이지로 강제이동됨
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    //@PostMapping("/create")
//    public String questionCreate(@RequestParam(value="subject") String subject, @RequestParam(value = "content") String content) {
//        //질문을 저장한다.
//        this.questionService.create(subject, content);
//        return "redirect:/question/list"; //질문 저장 후 질문목록으로 이동
//    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        //매개변수를 subject와 content 항목을 지닌 폼을 전송해서 자동으로 바인딩 되게 함.
        //폼의 바인딩 기능 - 이름이 동일하면 함게 연결되어 묶이는 것
        //Valid 애너테이션을 적용하면 QuestionForm의 @NotEmpth, @Size 검증 기능이 동작
        //BindingResult 매개변수는 @Valid 애너테이션으로 검증이 수행된 결과를 의미하는 객체
        //두 매개변수의 위치가 정확하지 않다면 @Valid만 적용되어 검증 실패 시 400 오류가 발생함

        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
        //질문 저장 후 질문목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        //수정할 질문의 제목과 내용을 화면에 보여주기 위해 questionForm 객체에
        //id 값으로 조회한 질무의 제목과 내용의 값을 담아서 템플릿으로 전달
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
        if(bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}