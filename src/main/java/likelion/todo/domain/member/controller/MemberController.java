package likelion.todo.domain.member.controller;

import likelion.todo.domain.member.dto.MemberLoginRequestDTO;
import likelion.todo.domain.member.dto.MemberLoginResponseDTO;
import likelion.todo.domain.member.dto.MemberRegisterRequestDTO;
import likelion.todo.domain.member.dto.MemberRegisterResponseDTO;
import likelion.todo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponseDTO> register(@RequestBody MemberRegisterRequestDTO req) {
        return ResponseEntity.ok(memberService.registerMember(req));
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDTO> login(@RequestBody MemberLoginRequestDTO req) {
        return ResponseEntity.ok(memberService.loginMember(req));
    }
}