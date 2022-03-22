package com.bcad.application.repository;

import com.bcad.application.domain.MakerPMSNav;
import com.bcad.application.domain.PMSNav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakerPMSNavRepository extends JpaRepository<MakerPMSNav, Long> {

    @Query(value="select * from maker_fee_pms_nav where fileuploaded_id=259", nativeQuery = true)
    List<MakerPMSNav> getDuplicateDate();


}
