package com.ck.file.dao;

import com.ck.file.dao.bean.FileInfoPo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoDataAccess extends JpaRepository<FileInfoPo, String> {

    @Query(value = "SELECT MAX(updateSNo) FROM FileInfoPo WHERE fileInfoPK.uaerAccount = ?1")
    Integer getMaxFileSnoWithAccount(String userAccount);

    @Query(value = "SELECT p FROM FileInfoPo p WHERE p.fileInfoPK.uaerAccount = ?1")
    List<FileInfoPo> getListByAccount(String userAccount);
}
