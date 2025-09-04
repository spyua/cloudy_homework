package com.ck.file.service;

import com.ck.file.dao.bean.FileInfoPo;
import com.ck.file.service.bean.FileInfoBo;
import com.ck.file.service.bean.FileInfoRelayBo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileProcessService {

    public void upload(byte[] file, FileInfoBo fileInfo);

    public byte[] getFile(FileInfoBo fileInfoBo);

    public byte[] getFile(FileInfoBo fileInfoBo, String buket);

    public List<FileInfoRelayBo> getFileList(String userAccount);

}
