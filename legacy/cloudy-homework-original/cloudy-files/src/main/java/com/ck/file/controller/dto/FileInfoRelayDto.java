package com.ck.file.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
public class FileInfoRelayDto {

    String fileName;

    Long compressFileSize;

    String fileExtestion;

    String originFileName;

    Long originFileSize;

    String createTime;

    String fileData;

}
