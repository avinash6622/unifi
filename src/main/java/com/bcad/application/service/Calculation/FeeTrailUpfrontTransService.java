package com.bcad.application.service.Calculation;

import com.bcad.application.domain.CumulativeDistributorReportBean;
import com.bcad.application.domain.DistributorMaster;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class FeeTrailUpfrontTransService {

    @PersistenceContext
    EntityManager entityManager;

    public CumulativeDistributorReportBean getDistributorCurrentReport(DistributorMaster distributorMaster, Float pmsCommission, Date startDate, Date toDate) {

        CumulativeDistributorReportBean cumulativeDistributorReportBean;
        Double trailAmt, upfrontAmt, payableAmt, paidAmt;
        Float distCommision;
        distCommision = pmsCommission / 100;
        // String tempTrailAmt,tempUpfrontAmt,tempPayableAmt,tempPaidAmt;
        Query query = entityManager.createNativeQuery(
            "select sum(t.trail_amt),sum(t.upfront_amt),sum(t.payable_amt),sum(t.paid_amt) from fee_trail_upfront_trans t where "
                + "t.dist_id=?2 and t.trans_date between ?3 and ?4 and t.int_details_updated_flag=0" + "");
        // query.setParameter(1, distCommision);
        query.setParameter(2, distributorMaster.getId());
        query.setParameter(3, startDate);
        query.setParameter(4, toDate);

        List<?> result = query.getResultList();
        Object[] row = (Object[]) result.get(0);
        trailAmt = (Double) row[0];
        if(trailAmt==null)
            trailAmt=0.0;
        System.out.println(trailAmt);
        upfrontAmt = (Double) row[1];
        if(upfrontAmt==null)
            upfrontAmt=0.0;
        payableAmt = (Double) row[2];
        if(payableAmt==null)
            payableAmt=0.0;
        paidAmt = (Double) row[3];
        if(paidAmt==null)
            paidAmt=0.0;


        cumulativeDistributorReportBean = new CumulativeDistributorReportBean();
        cumulativeDistributorReportBean.setCurrentTrailAmt(new BigDecimal(trailAmt));
        cumulativeDistributorReportBean.setCurrentUpfrontAmt(new BigDecimal(upfrontAmt));
        cumulativeDistributorReportBean.setCurrentTotalPayableAmt(new BigDecimal(payableAmt));
        cumulativeDistributorReportBean.setCurrentPaidAmount(new BigDecimal(paidAmt));

        return cumulativeDistributorReportBean;


    }
}
