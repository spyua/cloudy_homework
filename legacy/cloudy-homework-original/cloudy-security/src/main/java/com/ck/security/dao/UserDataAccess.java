package com.ck.security.dao;


import com.ck.security.dao.bean.AccountInfoPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataAccess extends JpaRepository<AccountInfoPo, String> {


}
