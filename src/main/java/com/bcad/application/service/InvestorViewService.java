package com.bcad.application.service;

import com.bcad.application.domain.*;
import com.bcad.application.repository.AIFClientMasterRepository;
import com.bcad.application.repository.InvestorViewRepository;
import com.bcad.application.repository.SeriesMasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InvestorViewService {

    private final SeriesMasterRepository seriesMasterRepository;
    private final InvestorViewRepository investorViewRepository;
    private final AIFClientMasterRepository aifClientMasterRepository;

    public InvestorViewService(SeriesMasterRepository seriesMasterRepository, InvestorViewRepository investorViewRepository,
                               AIFClientMasterRepository aifClientMasterRepository) {
        this.seriesMasterRepository = seriesMasterRepository;
        this.investorViewRepository=investorViewRepository;
        this.aifClientMasterRepository=aifClientMasterRepository;
    }

    @PersistenceContext
    EntityManager entityManager;

    public List<InvestorProtfolio> seriesCalc(String monthYear){
        List<InvestorProtfolio> investView=new ArrayList<>();
        Query query=entityManager.createNativeQuery("SELECT mnth_yr,series_id,sum(no_of_units) FROM `investor_portfolio` where mnth_yr=?1 group by series_id "
            + "order by series_id");
        query.setParameter("1", monthYear);

        System.out.println("month");
        List<?> result = query.getResultList();

        for(int i=0; i<result.size(); i++) {
            Object[] row = (Object[]) result.get(i);

            InvestorView investorViewDO=new InvestorView();
            investorViewDO.setMonthYear(row[0].toString());
            String noOf=row[2].toString();
            investorViewDO.setNoOfUnits(Float.parseFloat(noOf));
            Optional<SeriesMaster> seriesMaster =seriesMasterRepository.findById(Long.valueOf((row[1].toString())));
            investorViewDO.setSeriesMaster(seriesMaster.get());
            investorViewRepository.save(investorViewDO);
            System.out.println(row[0]+", "+ row[1]);
        }

        return investView;

    }

    public List<AIFManagePerMonth> managMonth(String sMonth, String sYear) {
        List<AIFManagePerMonth> managementValidate=new ArrayList<>();
        Query query=entityManager.createNativeQuery("SELECT * FROM fee_mange_perf_month WHERE MONTH(as_on_date) = ?1 "
            + " and YEAR(as_on_date) =?2 and int_deleted=0");
        query.setParameter("1", sMonth);
        query.setParameter("2", sYear);

        System.out.println("month");
        List<?> result = query.getResultList();
        managementValidate.addAll((Collection<? extends AIFManagePerMonth>) result);
        System.out.println(managementValidate.size());

        return managementValidate;
    }

    public int deleteSeriesMonth(Optional<FileUploadAIF> file) {
        int result=0;
        Query query=entityManager.createNativeQuery("update series_monthwise s set s.int_deleted=1 where s.file_upload_aif_id="+file.get().getId());
        result=query.executeUpdate();
        return result;
    }

    public int deleteManagePerformance(Long id) {
        int result=0;
        Query query=entityManager.createNativeQuery("update fee_mange_perf_month a set a.int_deleted=1 where a.file_upload_aif_id="+id);
        result=query.executeUpdate();
        return result;

    }

    public String getInvestorMonth() {
        String firstMonth;
        InvestorProtfolio investorProtfolio;
        Query query=entityManager.createNativeQuery("SELECT mnth_yr FROM `investor_portfolio` limit 1 ");
        firstMonth=(String) query.getSingleResult();
        return firstMonth;
    }

    public List<AIFUpdatedUnits> validateUpdatedPrevious(String vMonth) {
        List<AIFUpdatedUnits> updateValidate=new ArrayList<>();
        Query query=entityManager.createNativeQuery("SELECT * FROM aif_updated_units WHERE mnthyr = ?1 ");
        query.setParameter("1", vMonth);

        System.out.println("month");
        List<?> result = query.getResultList();
        updateValidate.addAll((Collection<? extends AIFUpdatedUnits>) result);
        System.out.println(updateValidate.size());

        return updateValidate;
    }

    public List<AIFUpdatedUnits> updateSeries(String sMonth, SeriesMaster seriesMaster) {

        List<AIFUpdatedUnits> updateViewSeries=new ArrayList<>();
        Query query=entityManager.createNativeQuery("SELECT mnthyr,series_id,sum(tot_rem_units) FROM `aif_updated_units` where mnthyr=?1 and "
            + "series_id=?2 group by series_id ");
        query.setParameter("1", sMonth);
        query.setParameter("2",seriesMaster);

        System.out.println("month");
        List<?> result = query.getResultList();

        for(int i=0; i<result.size(); i++) {
            Object[] row = (Object[]) result.get(i);

            InvestorView investorViewDO=new InvestorView();
            //investorViewDO.setMonthYear(row[0].toString());
            String noOf=row[2].toString();
            //investorViewDO.setNoOfUnits(Float.parseFloat(noOf));

            investorViewDO.setSeriesMaster(seriesMasterRepository.findById(Long.valueOf(row[1].toString())).get());
            investorViewDO=investorViewRepository.findByMonthYearAndSeriesMaster(sMonth,seriesMasterRepository.findById(Long.valueOf(row[1].toString())).get());
            investorViewDO.setNoOfUnits(Float.parseFloat(noOf));
            investorViewRepository.save(investorViewDO);
            System.out.println(row[0]+", "+ row[1]);
        }

        return updateViewSeries;


    }

    public List<AIFUpdatedUnits> seriesCalcInvestor(String sMonth) {

        List<AIFUpdatedUnits> investView=new ArrayList<>();
        Query query=entityManager.createNativeQuery("SELECT mnthyr,series_id,sum(tot_rem_units) FROM `aif_updated_units` where mnthyr=?1 group by series_id "
            + "order by series_id");
        query.setParameter("1", sMonth);

        System.out.println("month");
        List<?> result = query.getResultList();

        for(int i=0; i<result.size(); i++) {
            Object[] row = (Object[]) result.get(i);
            InvestorView investorViewDO=new InvestorView();
            investorViewDO.setMonthYear(row[0].toString());
            String noOf=row[2].toString();
            investorViewDO.setNoOfUnits(Float.parseFloat(noOf));
            Optional<SeriesMaster> seriesMaster=seriesMasterRepository.findById(Long.valueOf(row[1].toString()));
            investorViewDO.setSeriesMaster(seriesMaster.get());
            System.out.println(row[0]+", "+ row[1]);
            investorViewRepository.save(investorViewDO);

        }
        return investView;
    }

    public List<ReportAifDistBean> getDistFee(DistributorMaster masterDO, List<String> aifDate) {
        List<ReportAifDistBean> clubAifUpdatedUnit=new ArrayList<ReportAifDistBean>();

        Query query=entityManager.createNativeQuery("SELECT sum(tot_rem_units),series_id,"
            + "aif_client_id FROM aif_updated_units where mnthyr "
            + "in (?1) and dist_id=?2 group by series_id,"
            + "aif_client_id ORDER BY `aif_updated_units`.`aif_client_id` ASC");
        query.setParameter("2", masterDO);
        query.setParameter("1", aifDate);

        System.out.println("month");
        List<?> result = query.getResultList();
        for(int i=0; i<result.size(); i++) {
            Object[] row = (Object[]) result.get(i);
            ReportAifDistBean reportAifDist = new ReportAifDistBean();
            Optional<AIFClientMaster> aifClientMaster = null;
            Optional<SeriesMaster> seriesMaster = null;

            String noOf=row[0].toString();
            String seriesId=row[1].toString();
            String aifId=row[2].toString();
            System.out.println(noOf+"----"+seriesId+"----"+aifId);

            aifClientMaster=aifClientMasterRepository.findById(Long.valueOf(row[2].toString()));
            seriesMaster=seriesMasterRepository.findById(Long.valueOf(row[1].toString()));
            reportAifDist.setTotNoOfUnits(Float.parseFloat(row[0].toString()));
            reportAifDist.setClientCode(aifClientMaster.get().getClientCode());
            reportAifDist.setClientName(aifClientMaster.get().getClientName());
            reportAifDist.setSeriesName(seriesMaster.get().getSeriesCode());
            reportAifDist.setSeriesMaster(seriesMaster.get());
            clubAifUpdatedUnit.add(reportAifDist);
        }

        return clubAifUpdatedUnit;
    }
}
