package com.ck.file.service.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
public class FileInfoRelayBo {
    String originFileName;

    Long originFileSize;

    String fileName;

    Long compressFileSize;

    String fileExtension;

    String createDate;

    String fileData;



}
