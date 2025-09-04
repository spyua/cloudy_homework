package com.ck.file.dao.bean;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "TB_FILE_INFO",schema="HW_PROCESS")
public class FileInfoPo {

    @EmbeddedId
    FileInfoPK fileInfoPK;

    @Column(name="orin_file_name")
    String orinFileName;

    @Column(name="file_extension")
    String fileExtension;

    @Column(name="upd_sno")
    Integer updateSNo;

    @Column(name="create_time")
    Timestamp createdTime;

    @Column(name="update_time")
    Timestamp updateTime;

    @Column(name="file_status")
    Boolean fileStatus;

    @Column(name="file_size")
    Long fileSize;

}


