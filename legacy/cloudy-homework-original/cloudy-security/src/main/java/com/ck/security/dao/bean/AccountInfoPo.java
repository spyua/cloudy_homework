package com.ck.security.dao.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "TB_ACCOUNT_INFO",schema="HW_SECURITY")
public class AccountInfoPo {

    @Id
    @Column(name="user_account")
    String userAccound;

    @Column(name="user_password")
    String userPassword;

    @Column(name="create_time")
    Timestamp createTime;

    @Column(name="last_login")
    Timestamp lastLogin;
}
