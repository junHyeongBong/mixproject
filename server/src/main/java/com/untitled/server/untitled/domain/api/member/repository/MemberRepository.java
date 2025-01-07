package com.untitled.server.untitled.domain.api.member.repository;

import com.untitled.server.untitled.domain.entity.Member;
import com.untitled.server.untitled.global.common.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , JpaSpecificationExecutor<Member> {

    /**
     * LoginId로 사용자 찾기
     * @param loginId
     * @return
     */
    Optional<Member> findByLoginId(String loginId);

    /**
     * role로 사용자 찾기
     * @param role
     * @return
     */
    Member findByRole(Role role);

}
