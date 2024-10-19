/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.co.bvk.management.mapper;

import id.co.bvk.management.dto.MemberDto;
import id.co.bvk.management.models.Member;
import java.util.Base64;
import org.springframework.stereotype.Service;

/**
 *
 * @author Akbr
 */
@Service
public class MemberMapper {
    
    // Mengubah Entity ke DTO
    public MemberDto toDTO(Member member) {
//        String pictureBase64 = null;
//        if (member.getPicture() != null) {
//            pictureBase64 = Base64.getEncoder().encodeToString(member.getPicture());
//        }
        return new MemberDto(
                member.getId(),
                member.getName(),
                member.getPosition(),
                member.getReportsTo(),
                member.getPicture()
        );
    }

    // Mengubah DTO ke Entity
    public Member toEntity(MemberDto dto) {
        Member member = new Member();
        member.setName(dto.getName());
        member.setPosition(dto.getPosition());
        member.setReportsTo(dto.getReportsTo());
        member.setPicture(dto.getPictureBase64());
//        if (dto.getPictureBase64() != null && !dto.getPictureBase64().isEmpty()) {
//            byte[] picture = Base64.getDecoder().decode(dto.getPictureBase64());
//            member.setPicture(picture);
//        }
        return member;
    }
}
