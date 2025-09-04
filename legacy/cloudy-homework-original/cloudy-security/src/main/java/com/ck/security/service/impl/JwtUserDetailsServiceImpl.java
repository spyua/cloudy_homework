package com.ck.security.service.impl;

import com.ck.security.dao.UserDataAccess;
import com.ck.security.dao.bean.AccountInfoPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDataAccess userDataAccess;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountInfoPo accountInfoPo = userDataAccess.findById(username).orElseThrow(() -> new UsernameNotFoundException("Wrong of AccountID or Password"));
        return new User(accountInfoPo.getUserAccound(), accountInfoPo.getUserPassword(), new ArrayList<>());
    }

    public AccountInfoPo createAccount(String accountID, String password) {
        System.out.println("EnCdoe -------------");
        System.out.println(bcryptEncoder.encode(password));
        AccountInfoPo accountInfoPo = new AccountInfoPo();
        accountInfoPo.setUserAccound(accountID);
        accountInfoPo.setUserPassword(bcryptEncoder.encode(password));
        accountInfoPo.setCreateTime(new Timestamp(new Date().getTime()));

        return userDataAccess.save(accountInfoPo);

    }
}
