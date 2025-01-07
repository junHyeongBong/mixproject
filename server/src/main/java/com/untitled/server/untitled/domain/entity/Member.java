package com.untitled.server.untitled.domain.entity;

import com.untitled.server.untitled.global.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)  //아무런값도 갖지 않는 의미없는 객체생성 막음, 무분별한 객체 생성에 대해 체크할수있음
                                                    //@Builder는 클래스, 생성자에 붙일수있는데 @AllArgsConstructor 쓰지않아야함, 모든필드에 대한 생성자를 자동 생성
                                                    //인스턴스 멤버의 선언순서에 영향받기에 변수의 순서를 바꾸면 생성자 입력값 순서도 바뀌게 되어 오류 발생
                                                    //생성자에 @Builder 붙이기
@Getter
//@ToString(exclude = {"team"})   //연관관계 필드는 toString을 사용 X, 무한루프 발생할수있음, 연관관계필드를 제외하거나 연관 엔티티 식별 가능한값으로 대체, exclude를 해서 제외할 필드를 명시적으로 지정하는것이 좋음
@Entity
//@Setter   //객체 안전성 보장힘듦 Entity에서 @Setter사용시 변경가능성이 추적하기 힘듦 값변경이 필요한경우 의미있는 메서드 생성 사용
public class Member {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String picture;

    @Enumerated(EnumType.STRING)       // Enum타입 객체 쓸때 db값에 string 그대로인 값으로 넣어줌
    private Role role;

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    @Builder
    public Member(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }




}
