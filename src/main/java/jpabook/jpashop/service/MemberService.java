package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 모든 데이터 변경은 트랜잭션 안에서 수행되어야 한다., readOnly=true : 데이터의 변경이 없는 읽기 전용 메서드에 사용
// @AllArgsConstructor -> 생성자 주입해줌
@RequiredArgsConstructor // private final MemberRepository memberRepository 만으로 생성자를 만들어준다
public class MemberService {

    private final MemberRepository memberRepository;

    // 필드 주입 대신에 생성자 주입을 사용하자

    //@RequiredArgsConstructor을 사용하므로 주석 처리
    /*
    @Autowired => 생성자가 하나면  @Autowired 생략 가능
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    // 회원 가입
    @Transactional // 데이터 변경 가능하도록 readOnly = false
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
