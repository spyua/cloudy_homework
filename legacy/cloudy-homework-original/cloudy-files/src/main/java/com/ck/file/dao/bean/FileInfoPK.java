package com.ck.file.dao.bean;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfoPK {
    @Column(name="user_account")
    String uaerAccount;
    @Column(name="file_name")
    String fileName;
}
