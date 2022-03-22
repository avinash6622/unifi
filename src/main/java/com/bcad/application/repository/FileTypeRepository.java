package com.bcad.application.repository;

import com.bcad.application.domain.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType,Long> {

    FileType findByFileType(String fileType);
}
