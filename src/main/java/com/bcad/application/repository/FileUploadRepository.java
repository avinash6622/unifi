package com.bcad.application.repository;
import com.bcad.application.domain.FileType;
import com.bcad.application.domain.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload,Long> {


    FileUpload findByFileLocationAndIsDeletedAndFileType(String fileLocation, Integer isDeleted, FileType fileType);

    Page<FileUpload>  findByIsDeleted(int i,Pageable pageable);

    FileUpload findByFileLocationAndIsDeleted(String toString, int i);
}
