package com.bcad.application.repository;

import com.bcad.application.domain.BCADProfitShare;
import com.bcad.application.domain.ClientManagement;
import com.bcad.application.domain.ProfitShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BCADProfitShareRepository extends JpaRepository<BCADProfitShare, Long> {
    BCADProfitShare findByClientManagementAndReceiptDateBetweenAndIsDeleted(ClientManagement clientManagement, Date startDate, Date endDate, Integer isDeleted);

}
