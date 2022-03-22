package com.bcad.application.service;

import com.bcad.application.domain.DistributorMaster;
import com.bcad.application.domain.FileUpload;
import com.bcad.application.domain.ProfitShare;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Service
public class PMSService {

    @PersistenceContext
    EntityManager entityManager;

    public Date onStartDate(FileUpload fileUpload) {
        Date pDate;
        Query q = entityManager.createNativeQuery("select start_date FROM fee_pms_profit_share where fileuploaded_id=?1 limit 1");
        q.setParameter(1, fileUpload);
        pDate = (Date) q.getSingleResult();
        return pDate;
    }

    public int deleteProfitShare(FileUpload fileUpload) {
        int result = 0;
        Query query = entityManager.createNativeQuery("update fee_pms_profit_share p set p.int_deleted=1 where p.fileuploaded_id=" + fileUpload.getId());
        result = query.executeUpdate();
        return result;
    }

    public Date onBrokerageStartDate(FileUpload fileUpload) {
        Date pDate;
        String query = "SELECT start_date FROM fee_brokerage where fileuploaded_id=?1 limit 1";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter(1, fileUpload);
        pDate = (Date) q.getSingleResult();
        return pDate;
    }

    public int deleteBrokerage(FileUpload fileUpload) {
        int result = 0;
        Query query = entityManager.createNativeQuery("update fee_brokerage p set p.int_deleted=1 where p.fileuploaded_id=" + fileUpload.getId());
        result = query.executeUpdate();
        return result;
    }

    public Date onPMSStartDate(FileUpload fileUpload) {
        Date pDate;
        String query = "SELECT start_date FROM fee_pms_nav where fileuploaded_id=?1 limit 1";
        Query q = entityManager.createNativeQuery(query);
        q.setParameter(1, fileUpload);
        pDate = (Date) q.getSingleResult();
        return pDate;
    }

    public int deletePmsNav(FileUpload fileUpload) {
        int result = 0;
        Query query = entityManager.createNativeQuery("update fee_pms_nav p set p.int_deleted=1 where p.fileuploaded_id=" + fileUpload.getId());
        result = query.executeUpdate();
        System.out.println(result);
        return result;
    }


    public List<ProfitShare> onProfitShareByDistBetween(DistributorMaster distributorMaster, Date startDate, Date endDate) {
        int result = 0;
       /* String query = "select ps.* from fee_pms_profit_share ps where int_deleted=0  and receipt_date between ?2 and ?3 and client_id in(select id from pms_client_master where dist_id=?1)";
        Query q = entityManager.createNativeQuery(query,ProfitShare.class);
        q.setParameter(1, distId);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);
         List<ProfitShare> result1 =q.getResultList() ;*/
        List<ProfitShare> result1 ;
        String query="SELECT p from ProfitShare p where p.isDeleted=0 and p.pmsClientMaster.distributorMaster.id=?1 and p.receiptDate between ?2 and ?3";
        Query q=entityManager.createQuery(query);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);
        q.setParameter(1, distributorMaster.getId());
         q.getResultList();
        System.out.println(q.getResultList());
        return null;


    }
}
