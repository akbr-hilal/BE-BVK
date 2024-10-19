package id.co.bvk.management.services;

import id.co.bvk.management.dto.MemberDto;
import id.co.bvk.management.mapper.MemberMapper;
import id.co.bvk.management.models.Member;
import id.co.bvk.management.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Akbr
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<MemberDto> getMemberById(Long id) {
        return memberRepository.findById(id).map(memberMapper::toDTO);
    }

    // Create Member (Menerima DTO)
    @Transactional
    public Member createMember(MemberDto dto) {
        Member member = memberMapper.toEntity(dto);
        Member savedMember = null;
        try {
            savedMember = memberRepository.save(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return member;
    }

    // Update Member (Menerima DTO)
    public MemberDto updateMember(Long id, MemberDto dto) {
        return memberRepository.findById(id).map(member -> {
            Member updatedMember = memberMapper.toEntity(dto);
            updatedMember.setId(member.getId());  // Set ID yang sudah ada
            Member savedMember = memberRepository.save(updatedMember);
            return memberMapper.toDTO(savedMember);
        }).orElseThrow(() -> new RuntimeException("Member not found with id [" + id + "]"));
    }

    // Delete Member
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

}
