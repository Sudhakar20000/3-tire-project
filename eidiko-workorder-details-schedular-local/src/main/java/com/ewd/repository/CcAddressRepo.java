package com.ewd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ewd.entity.ToAddress;

public interface CcAddressRepo extends JpaRepository<ToAddress, Long>{

	 @Query("SELECT t.email FROM CcAddress t")
	 List<String> findCcAddress();
}
