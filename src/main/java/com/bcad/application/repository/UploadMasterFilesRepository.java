package com.bcad.application.repository;
import com.bcad.application.domain.FileUploadAIF;
import com.bcad.application.domain.MasterType;
import com.bcad.application.domain.UploadMasterFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadMasterFilesRepository extends JpaRepository<UploadMasterFiles,Long> {

    UploadMasterFiles findByFileLocationAndIsDeletedAndMasterType(String sPath, Integer isDeleted, MasterType masterType);

    UploadMasterFiles findByFileLocationAndIsDeleted(String sPath, Integer isDeleted);

    Page<UploadMasterFiles> findByIsDeleted(int i, Pageable pageable);
}
