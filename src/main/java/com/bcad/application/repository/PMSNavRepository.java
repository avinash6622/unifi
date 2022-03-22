package com.bcad.application.repository;

import com.bcad.application.domain.FileUpload;
import com.bcad.application.domain.PMSClientMaster;
import com.bcad.application.domain.PMSNav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PMSNavRepository extends JpaRepository<PMSNav, Long> {

    List<PMSNav> findByPmsClientMasterAndStartDateBetweenAndIsDeleted(PMSClientMaster pmsClientMaster, Date startDate, Date endDate, Integer isDeleted);

    @Query(value=" select sum(calc_pms_nav) from fee_pms_nav where pms_client_id=:pmsClientId and selected_start_date between :startDate and :endDate " +
        " and investment_date<'2020-04-01' and int_deleted=0;",nativeQuery = true)
    Double getPMSFeeBeforeDate(@Param("pmsClientId")Long pmsClientId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    @Query(value=" select sum(calc_pms_nav) from fee_pms_nav where pms_client_id=:pmsClientId and selected_start_date=:startDate" +
        " and investment_date<'2020-04-01' and int_deleted=0;",nativeQuery = true)
    Double getPMSFeeMonthWise(@Param("pmsClientId")Long pmsClientId,@Param("startDate")String startDate);

    //List<PMSNav> findByPmsClientMasterAndSelectedStartDateAndIsDeleted(PMSClientMaster pmsClientMaster, Date startDate, Integer isDeleted);

    @Query(value = "SELECT start_date FROM fee_pms_nav where int_deleted=0 and client_code_scheme=:codeScheme and selected_start_date<'2020-04-01' order by start_date asc limit 1", nativeQuery = true)
    Date getPMSNavInvestment(@Param("codeScheme")String codeScheme);

    @Query(value = "SELECT start_date FROM fee_pms_nav where int_deleted=0 and client_code_scheme=:clientCode  and selected_start_date " +
        "< :investDate order by start_date asc limit 1", nativeQuery = true)
    Date getPMSNavInvestmentDate(@Param("clientCode")String clientCode, @Param("investDate")Date investDate);

    @Query(value = "select IF(DATEDIFF(start_date, investment_date)=0,start_date,investment_date) as selected_start_date FROM fee_pms_nav " +
        "where int_deleted=0 and client_code_scheme = :clientCode and selected_start_date < :investDate and investment_date is not null order by " +
        "start_date asc limit 1 ", nativeQuery = true)
    Date getPMSNavInvestmentInitialDate(@Param("clientCode")String clientCode, @Param("investDate")Date investDate);

    @Query(value = "SELECT start_date FROM fee_pms_nav where int_deleted=0 and client_code_scheme=:codeScheme  and selected_start_date " +
        "< :investDate order by start_date asc limit 1", nativeQuery = true)
    Date getPMSNavInvestmentOldDate(@Param("codeScheme")String codeScheme,@Param("investDate")Date investDate);

    @Query(value="select id from bcad_product where product_name in (select investment_name from investment_master where id in (select distinct(investment_id) from fee_pms_nav fp," +
        "pms_client_master pm where fp.pms_client_id = pm.id and pm.dist_id=:distId and int_deleted=0 and selected_start_date between :startDate and :endDate))",nativeQuery = true)
    List<Long> getDistributorStrategy(@Param("distId")Long distId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    @Query(value="select client_code_scheme as clientCodeScheme,sum(calc_pms_nav) as pmsNav,investment_id as investment,investment_date as investDate from fee_pms_nav " +
        "where pms_client_id=:clientId and start_date between :startDate and :endDate and int_deleted=0 group by client_code_scheme,investment_id,investment_date order  by client_code_scheme asc",nativeQuery = true)
    List<Object[]> getClientCodeStrategy(@Param("clientId")Long clientId,@Param("startDate")String startDate,@Param("endDate")String endDate);

     List<PMSNav> findByFileUploadAndCodeScheme(FileUpload fileUpload,String clientCodeScheme);

     @Query(value="SELECT * FROM fee_pms_nav where fileuploaded_id in(295);", nativeQuery = true)
     List<PMSNav> findByUploadId();

    List<PMSNav> findByFileUpload(FileUpload fileUpload);
}
