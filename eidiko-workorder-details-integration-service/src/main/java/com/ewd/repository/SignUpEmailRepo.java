package com.ewd.repository;

import com.ewd.entity.SignUpEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SignUpEmailRepo extends JpaRepository<SignUpEmail, Long>{

@Query("SELECT m.email FROM SignUpEmail m")
List<String> findSignUpEmail();

}
