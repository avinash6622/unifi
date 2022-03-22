package com.bcad.application.config;

import com.bcad.application.domain.ReportBcadMonthlyCalculation;
import com.bcad.application.repository.RelationshipManagerRepository;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.bcad.application.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.bcad.application.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ClientManagement.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.Role.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.DistributorMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.DistributorMaster.class.getName() + ".distributorMasterMap", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.DistributorMaster.class.getName() + ".distributorMasterOption3", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.DistributorType.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.RelationshipManager.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.RelationshipManager.class.getName() + ".subRMS",
                jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.SubRM.class.getName() + ".subRMMap", jcacheConfiguration);
            cm.createCache(RelationshipManagerRepository.RELATIONS_ID, jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.SubRM.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.DistributorOption.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportGeneration.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.PMSClientMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIFClientMaster.class.getName(), jcacheConfiguration);
            cm.createCache(ReportBcadMonthlyCalculation.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ClientFeeCommission.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.SeriesMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.InvestmentMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.FileType.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIFManagePerMonth.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.FileUpload.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MakerBrokerage.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.Brokerage.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.RoleNameMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.Role.class.getName() + ".roleNameMasters", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.RoleNameMaster.class.getName() + ".roles", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ProfitShare.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MakerProfitShare.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.PMSNav.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MakerPMSNav.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.CommissionDefinition.class.getName() + ".distributorMasters", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.CommissionDefinition.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.CommissionDefinition.class.getName() + ".commissionDefinitionOptionMaps", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.CommissionDefinitionOptionMap.class.getName() + ".commissionDefinition", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.CommissionDefinition.class.getName() + ".distributorMasterOption", jcacheConfiguration);
           /* cm.createCache(com.bcad.application.domain.CommissionDefinition.class.getName() + ".relationshipManagers", jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.RelationshipManager.class.getName() + ".relationshipManagerMap", jcacheConfiguration);*/
            cm.createCache(com.bcad.application.domain.Trailupfrontpay.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.FileUploadUpfront.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.FeeTrailUpfrontTrans.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.DistributorOpeningBal.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIFRedemptionMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MakerUpfrontMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportsDistributorFee.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportAIFCalc.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportAIFQuarterFour.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportAIFQuarterFourFinancial.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportAIFQuarterFourPer.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportBrokeragePMS.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportProfitShare.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIFUpdatedUnits.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.FileUploadAIF.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.InvestorProtfolio.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.InvestorView.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MakerSeriesMasterMonth.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MakerAIFManagePerMonth.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.UpfrontMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MasterType.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.UploadMasterFiles.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.SeriesMasterMonth.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.BCADMakerPMSNav.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.BcadMakerUpfrontMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.BcadUpfrontMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.BCADPMSNav.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.BCADMakerProfitShare.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.BCADProfitShare.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.CommissionDefinitionOptionMap.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIF2SeriesMaster.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIF2ManagementFee.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIF2Investments.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIF2MonthlyCalculation.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportAIFQuarterFourManage.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ReportAIFQuarterFourPerformance.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIFBlendMonthlyCalculation.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.GenericPayTrailUpfront.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.BCADDistributorFee.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIFDistributorFee.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.MakerCapitalTransaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.CapitalTransaction.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.ClientCommission.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.TransactionReport.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.AIFUmbrella.class.getName(), jcacheConfiguration);
            cm.createCache(com.bcad.application.domain.UmbrellaReports.class.getName(),jcacheConfiguration);


            // jhipster-needle-ehcache-add-entry BcadUpfrontMaster
        };
    }
}
