package com.bcad.application.repository;
import com.bcad.application.domain.FileUploadUpfront;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadUpfrontRepository extends JpaRepository<FileUploadUpfront, Long> {

    Page<FileUploadUpfront> findByIsDeleted(int i, Pageable pageable);

    FileUploadUpfront findByStroreFileLocationAndIsDeleted(String sPath, int i);

    FileUploadUpfront findByStroreFileLocation(String sPath);
}
