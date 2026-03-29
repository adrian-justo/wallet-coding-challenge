package com.wcc.bookkeeping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wcc.bookkeeping.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

}