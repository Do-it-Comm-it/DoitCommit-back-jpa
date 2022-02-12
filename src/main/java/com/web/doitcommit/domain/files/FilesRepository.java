package com.web.doitcommit.domain.files;

import com.web.doitcommit.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FilesRepository extends JpaRepository<Files, Long> {

}
