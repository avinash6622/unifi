package com.bcad.application.repository;
import com.bcad.application.domain.FileUploadAIF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadAIFRepository extends JpaRepository<FileUploadAIF,Long> {

    FileUploadAIF findByFileLocationAndIsDeleted(String filePath, Integer isDeleted);

    Page<FileUploadAIF> findByIsDeleted(int i, Pageable pageable);
}
