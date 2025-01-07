package com.untitled.server.untitled.domain.api.member.service.impl;

import com.untitled.server.untitled.domain.api.member.service.IMemberService;
import com.untitled.server.untitled.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements IMemberService {

    @Override
    public List<Member> getMemberAll() {
        return List.of();
    }
}
