package com.ck.file.controller;

import com.ck.file.controller.dto.FileInfoDto;
import com.ck.file.controller.dto.FileInfoRelayDto;
import com.ck.file.service.FileProcessService;
import com.ck.file.service.bean.FileInfoBo;
import com.ck.file.service.bean.FileInfoRelayBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "*",allowedHeaders = "Authorization")
public class FilePorcessController {

    @Autowired
    FileProcessService fileProcessService;


    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        FileInfoBo fileInfo = new FileInfoBo();

        fileInfo.setUserAccount(authentication.getName());
        fileInfo.setOriginFileName(file.getOriginalFilename());
        fileInfo.setFileExtension(getExtension(file.getOriginalFilename(), ""));
        fileInfo.setFileSize(file.getSize());
        fileProcessService.upload(file.getBytes(), fileInfo);

    }


    @PostMapping("/getfile")
    public ResponseEntity<ByteArrayResource> getfile(@RequestBody FileInfoDto fileInfoDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        FileInfoBo fileInfoBo = new FileInfoBo();
        fileInfoBo.setFileName(fileInfoDto.getFileName());
        fileInfoBo.setUserAccount(authentication.getName());

        byte[] fileBytes = fileProcessService.getFile(fileInfoBo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileInfoDto.getOriginFileName());

        // 使用ByteArrayResource將byte array包裝成ResponseEntity物件
        ByteArrayResource resource = new ByteArrayResource(fileBytes);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);


    }

    @PostMapping(value = "/getFileList")
    public ResponseEntity<List<FileInfoRelayDto>> getFileList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<FileInfoRelayDto> result = new ArrayList<>();
        List<FileInfoRelayBo> relayBoLit = fileProcessService.getFileList(authentication.getName());
        relayBoLit.forEach(x -> {
            FileInfoRelayDto relayDto = new FileInfoRelayDto();
            relayDto.setFileName(x.getFileName());
            relayDto.setOriginFileName(x.getOriginFileName());
            relayDto.setFileExtestion(x.getFileExtension());
            relayDto.setCreateTime(x.getCreateDate());
            relayDto.setFileData(x.getFileData());
            relayDto.setCompressFileSize(x.getCompressFileSize());
            relayDto.setOriginFileSize(x.getOriginFileSize());
            result.add(relayDto);
        });


        return ResponseEntity.ok(result);
    }

    private String getExtension(String filename, String orElse) {
        return Optional.ofNullable(filename)
                .filter(s -> s.contains("."))
                .map(s -> s.substring(s.lastIndexOf(".") + 1))
                .orElse(orElse);
    }
}
