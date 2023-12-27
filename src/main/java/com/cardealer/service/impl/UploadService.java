package com.cardealer.service.impl;

import com.google.cloud.storage.*;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UploadService {
    // bucketname
    private final String bucketName = "upload-firebase-injava.appspot.com";
    // lấy ra đường dẫn gốc trên server
    private final ServletContext servletContext;
    private final Storage storage;

    public String uploadFile(MultipartFile fileUpload) {
        // tạo đường dẫn đến thư mục uploads
        String uploadPath = servletContext.getRealPath("/uploads");
        // kiểm tra thư mục có tồn tại không
        File file = new File(uploadPath);

        if (!file.exists()) {
            file.mkdirs();// tạo thự mục mới
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        // upload lên server
        String fileName = dtf.format(LocalDateTime.now()) + fileUpload.getOriginalFilename();
        try {
            FileCopyUtils.copy(fileUpload.getBytes(), new File(uploadPath + File.separator + fileName));
            return uploadFileFromServerToFirebase(uploadPath + File.separator + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // upload file lên firebase
    private String uploadFileFromServerToFirebase(String filePath) {
        Path localPath = Paths.get(filePath); // lấy ra đối tượng Paths của ảnh vừa upload lên server
        String fileName = localPath.getFileName().toString(); // lấy ra tên file upload

        BlobId blobId = BlobId.of(bucketName, fileName); // tạo file trên storage bằng tên và bucketname chỉ đinh

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        // Thiết lập quyền truy cập công cộng
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        blobInfo = blobInfo.toBuilder().setAcl(acls).build();
        try {
            Blob blob = storage.create(blobInfo, Files.readAllBytes(localPath));
            return blob.getMediaLink(); // trả về đường dẫn ảnh online
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> uploadFileToServer(List<MultipartFile> list){
        // tạo đường dẫn đến thư mục uploads
        String uploadPath = servletContext.getRealPath("/uploads");
        // kiểm tra thư mục có tồn tại không
        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();// tạo thự mục mới
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        // upload lên server
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile f: list) {
            String fileName = dtf.format(LocalDateTime.now())+f.getOriginalFilename();
            try {
                FileCopyUtils.copy(f.getBytes(),new File(uploadPath+File.separator+fileName));
                imageUrls.add(uploadPath+File.separator+fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // upload lên cloud firebase
        return  imageUrls.stream()
                .map(this::uploadFileFromServerToFirebase)
                .collect(Collectors.toList());
    }
}
