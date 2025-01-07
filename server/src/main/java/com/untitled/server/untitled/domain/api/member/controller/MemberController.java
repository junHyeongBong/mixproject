package com.untitled.server.untitled.domain.api.member.controller;

import com.untitled.server.untitled.domain.api.member.service.IMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member Controller", description = "member 관련 api controller")
@RestController                     //@Controller+@ResponseBody 추가
@RequiredArgsConstructor            //생성자 주입 , final변수, 필드 매개변수 생성자 자동 생성
@RequestMapping("/api/member")
//@Controller     //@Controller는 spring mvc에서 view반환하기 위해 사용 , Data를 반환할때는 @ResponseBody로 json형태로 데이터 반환
public class MemberController {

    private final IMemberService memberService;

//    @RequiredArgsConstructor를 안쓸시
//    @Autowired
//    public MemberController(IMemberService memberService) {
//        this.memberService = memberService;
//    }






}
