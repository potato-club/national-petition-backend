package com.example.nationalpetition.domain.member.repository;

import com.example.nationalpetition.domain.member.entity.DeleteMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteMemberRepository extends JpaRepository<DeleteMember, Long> {
}
