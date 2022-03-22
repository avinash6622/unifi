package com.bcad.application.repository;

import com.bcad.application.domain.FileUploadUpfront;
import com.bcad.application.domain.MakerUpfrontMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MakerUpfrontMasterRepository extends JpaRepository<MakerUpfrontMaster,Long> {

    List<MakerUpfrontMaster> findByFileUploadUpfront(FileUploadUpfront fileUploadUpfront);
}
