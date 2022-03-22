package com.bcad.application.web.rest;

import com.bcad.application.domain.ClientManagement;
import com.bcad.application.domain.FileType;
import com.bcad.application.repository.FileTypeRepository;
import com.codahale.metrics.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileTypeResource {

    private final FileTypeRepository fileTypeRepository;

    public FileTypeResource(FileTypeRepository fileTypeRepository) {
        this.fileTypeRepository = fileTypeRepository;
    }

    @GetMapping("/file-type")
    @Timed
    public List<FileType> getAllFileTypes() {
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        return fileTypeList;
    }
}
