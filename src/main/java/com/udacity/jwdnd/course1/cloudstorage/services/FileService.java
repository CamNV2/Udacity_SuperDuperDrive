package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getListFileByUserId(Integer userId){
        return fileMapper.getListFileByUserId(userId);
    }

    public void addFile(MultipartFile fileUpload, Integer userid) throws IOException {
        File file = new File();
        file.setContentType(fileUpload.getContentType());
        file.setFileData(fileUpload.getBytes());
        file.setFileName(fileUpload.getOriginalFilename());
        file.setFileSize(Long.toString(fileUpload.getSize()));
        file.setUserId(userid);

        fileMapper.addFile(file);
    }

    public boolean isExistFile(String fileName, Integer userId) {
        File file = fileMapper.checkFile(fileName,userId);
        if (file != null){
            return false;
        }
        return true;
    }

    public void deleteFile(Integer fileId){
        fileMapper.deleteFileById(fileId);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }
}
