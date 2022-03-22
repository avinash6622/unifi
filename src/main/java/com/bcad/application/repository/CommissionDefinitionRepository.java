package com.bcad.application.repository;

import com.bcad.application.domain.CommissionDefinition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionDefinitionRepository extends JpaRepository<CommissionDefinition, Long> {

    /*@Query(value = "select comm_id from bcad_comm_dist_map where  dist_id=:id ", nativeQuery = true)
    Long findDistributor(@Param("id") Long id);*/

    @Query(value = "select * FROM `bcad_commission_def` where product_id=:productId and id in(select comm_id from bcad_comm_dist_map where dist_id=:distId)", nativeQuery = true)
    List<CommissionDefinition> findCommissionss(@Param("distId")Long distId,@Param("productId")Long productId);

    @Query(value = "select * FROM `bcad_commission_def` where product_id=:productId and id in(select comm_id from bcad_comm_dist_map where dist_id=:distId)", nativeQuery = true)
    CommissionDefinition findCommissions(@Param("distId")Long distId,@Param("productId")Long productId);

    @Query(value = "select dist_id from bcad_comm_dist_map where dist_id in(:distIds) and comm_id in(SELECT id FROM " +
        "`bcad_commission_def` where product_id=:productId and id not in(:commId)) ", nativeQuery = true)
    List<Integer> findDistributorDuplications(@Param("productId") Long id, @Param("distIds") List<Long> idList,@Param("commId") Long commissionDefinitionId);

    @Query(value = "select dist_id from bcad_comm_dist_map where dist_id in(:distIds) and comm_id in(SELECT id FROM " +
        "`bcad_commission_def` where product_id=:productId and pms_investment=:pmsId and id not in(:commId)) ", nativeQuery = true)
    List<Integer> findDistributorPMSDuplications(@Param("productId") Long id,@Param("pmsId") Integer pmsId, @Param("distIds") List<Long> idList,@Param("commId") Long commissionDefinitionId);


    @Query(value = "select dist_id from bcad_comm_dist_opt3 where dist_id in(:distIds) and comm_id in(SELECT id FROM " +
        "`bcad_commission_def` where product_id=:productId and bcad_pms=1 and pms_investment=:pmsInvest and id not in(:commId)) ", nativeQuery = true)
    List<Integer> findDistributorDuplicationnOption3(@Param("productId") Long id, @Param("distIds") List<Long> idList,@Param("commId") Long commissionDefinitionId,@Param("pmsInvest")Integer pmsInvest);


    @Query(value = "select * FROM `bcad_commission_def` where product_id=:productId and pms_investment in(0,1) and id in(select comm_id from bcad_comm_dist_opt3 where dist_id=:distId)", nativeQuery = true)
    List<CommissionDefinition> findOption3Commission(@Param("distId")Long distId,@Param("productId")Long productId);

    @Query(value="select * FROM `bcad_commission_def` where product_id in(:productIds) and id in(select comm_id from bcad_comm_dist_map where dist_id=:distId)",nativeQuery = true)
    List<CommissionDefinition> getPMSStrategyCommission(@Param("productIds")List<Long> productIds,@Param("distId")Long distId);

    @Query(value="select * FROM `bcad_commission_def` where product_id =:productId and pms_investment=:pmsInvest and id in(select comm_id from bcad_comm_dist_map where dist_id=:distId)",nativeQuery = true)
    CommissionDefinition getPMSInvestmentDateCalc(@Param("distId")Long distId,@Param("pmsInvest")Integer pmsInvest,@Param("productId")Long productId);

    @Query(value="select * FROM `bcad_commission_def` where product_id =:productId and pms_investment=:pmsInvest and id in(select comm_id from bcad_comm_dist_opt3 where dist_id=:distId)",nativeQuery = true)
    CommissionDefinition getBCADInvestmentDateCalc(@Param("distId")Long distId,@Param("pmsInvest")Integer pmsInvest,@Param("productId")Long productId);

    @Query(value="select * FROM `bcad_commission_def` where product_id = :productId and id in(select comm_id from bcad_comm_dist_opt3 where dist_id=:distId)",nativeQuery = true)
    List<CommissionDefinition> getPMSOption3StrategyCommission(@Param("productId")Long productId,@Param("distId")Long distId);


    @Query(value="select comm_id from bcad_comm_dist_opt3 where dist_id=:distId" ,nativeQuery = true)
    List<Integer> Option3Check(@Param("distId") Long distId);
}
