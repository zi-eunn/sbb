package com.mysite.sbb.user;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //get으로 요청되면 회원 가입을 위한 템플릿을 렌더링
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    //post로 요청되면 회원가입을 진행하도록
    @PostMapping("/signup")
    //password1과 password2가 동일한지 검증하는 조건문 추가
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            //필드명, 오류 코드, 오류 메시지
            //여기서 오류 코드는 임의로 passwordInCorrect로 정의함
            return "signup_form";
        }
        try {

            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            //사용자 ID 또는 이메일 주소가 이미 존재할 경우 위의 예외 발생
            //'이미 등록된 사용자입니다'라는 오류 메시지를 화면에 표시
            e.printStackTrace();
            bindingResult.reject("signupFaild", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            //그 밖에 다른 예외들은 해당 예외에 관한 구체적인 오류 메시지를 출력하도록 e.getMesasge() 사용
            //여기서 bindingResult.reject(오류 코드, 오류 메시지)는 UserCreateForm의 검증에 의한 오류 외 일반적인 오류
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
