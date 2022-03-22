package com.bcad.application.repository;

import com.bcad.application.domain.BCADPMSNav;
import com.bcad.application.domain.ClientManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BCADPMSNavRepository extends JpaRepository<BCADPMSNav, Long> {

    List<BCADPMSNav> findByPercentageComm(Float optionValue);

    List<BCADPMSNav> findByClientManagementAndSelectedStartDateBetweenAndIsDeleted(ClientManagement clientManagement, Date startDate, Date endDate,Integer isDeleted);

    @Query(value="SELECT * FROM bcad_fee_pms_nav where fileuploaded_id in(135,137) and investment_date is null", nativeQuery = true)
    List<BCADPMSNav> getBCADDate();

    @Query(value="SELECT * FROM bcad_fee_pms_nav where selected_start_date between '2020-04-01' and '2020-04-30' and int_deleted=0",nativeQuery = true)
    List<BCADPMSNav> getAllClientsBetweenDates();
}
