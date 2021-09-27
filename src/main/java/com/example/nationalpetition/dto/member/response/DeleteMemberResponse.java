package com.example.nationalpetition.dto.member.response;

import com.example.nationalpetition.domain.member.entity.DeleteMember;
import com.example.nationalpetition.dto.member.DeleteMessageConst;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;

@Getter
@NoArgsConstructor
public class DeleteMemberResponse {

    private String name;
    private String email;
    private LocalDateTime deletedDate;
    private String message;

    public DeleteMemberResponse(String name, String email) {
        this.name = name;
        this.email = email;
        this.deletedDate = now();
        this.message = DeleteMessageConst.MESSAGE;
    }

    public static DeleteMemberResponse of(DeleteMember deleteMember) {
        return new DeleteMemberResponse(deleteMember.getName(), deleteMember.getEmail());
    }
}
