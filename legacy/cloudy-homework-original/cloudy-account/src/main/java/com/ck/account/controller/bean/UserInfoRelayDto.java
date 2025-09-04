package com.ck.account.controller.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRelayDto {

    String token;

    public UserInfoRelayDto(String token) {
        this.token = token;
    }
}
