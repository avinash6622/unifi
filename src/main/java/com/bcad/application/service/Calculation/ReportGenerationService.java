package com.bcad.application.service.Calculation;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.web.rest.util.StyleUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ReportGenerationService {

    @PersistenceContext
    EntityManager entityManager;


    private final ReportBcadMonthlyCalculationRepository reportBcadMonthlyCalculationRepository;
    private final ClientManagementRepository clientManagementRepository;
    private final DistributorOptionRepository distributorOptionRepository;
    private final BcadUpfrontMasterRepository bcadUpfrontMasterRepository;
    private final BCADPMSNavRepository bcadpmsNavRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;
    private final BCADProfitShareRepository bcadProfitShareRepository;
    private final ProductRepository productRepository;
    private final AIF2MonthlyCalculationRepository aif2MonthlyCalculationRepository;
    private final AIF2ManagementFeeRepository aif2ManagementFeeRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final AIF2InvestmentsRepository aif2InvestmentsRepository;
    private final AIFBlendMonthlyCalculationRepository aifBlendMonthlyCalculationRepository;
    private final PMSAndAIFReportService pmsAndAIFReportService;
    private final BCADDistributorFeeRepository bcadDistributorFeeRepository;
    private final GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository;
    private final AIFDistributorFeeRepository aifDistributorFeeRepository;
    private final PMSStrategyWiseService pmsStrategyWiseService;
    private final ClientCommissionRepository clientCommissionRepository;
    private final TransactionReportRepository transactionReportRepository;
    private final AifUmbrellaCalculationRepository aifUmbrellaCalculationRepository;
    private final AIF2SeriesMasterRepository aif2SeriesMasterRepository;

    public ReportGenerationService(ReportBcadMonthlyCalculationRepository reportBcadMonthlyCalculationRepository, ClientManagementRepository clientManagementRepository, DistributorOptionRepository distributorOptionRepository, BcadUpfrontMasterRepository bcadUpfrontMasterRepository,
                                   BCADPMSNavRepository bcadpmsNavRepository, CommissionDefinitionRepository commissionDefinitionRepository, BCADProfitShareRepository bcadProfitShareRepository, ProductRepository productRepository, AIF2MonthlyCalculationRepository aif2MonthlyCalculationRepository,
                                   AIF2ManagementFeeRepository aif2ManagementFeeRepository, DistributorMasterRepository distributorMasterRepository, AIF2InvestmentsRepository aif2InvestmentsRepository,
                                   PMSAndAIFReportService pmsAndAIFReportService, AIFBlendMonthlyCalculationRepository aifBlendMonthlyCalculationRepository,
                                   BCADDistributorFeeRepository bcadDistributorFeeRepository, GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository, AIFDistributorFeeRepository aifDistributorFeeRepository,
                                   PMSStrategyWiseService pmsStrategyWiseService, ClientCommissionRepository clientCommissionRepository, TransactionReportRepository transactionReportRepository,
                                   AifUmbrellaCalculationRepository aifUmbrellaCalculationRepository, AIF2SeriesMasterRepository aif2SeriesMasterRepository) {
        this.reportBcadMonthlyCalculationRepository = reportBcadMonthlyCalculationRepository;
        this.clientManagementRepository = clientManagementRepository;
        this.distributorOptionRepository = distributorOptionRepository;
        this.bcadUpfrontMasterRepository = bcadUpfrontMasterRepository;
        this.bcadpmsNavRepository = bcadpmsNavRepository;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
        this.bcadProfitShareRepository = bcadProfitShareRepository;
        this.productRepository = productRepository;
        this.aif2MonthlyCalculationRepository = aif2MonthlyCalculationRepository;
        this.aif2ManagementFeeRepository = aif2ManagementFeeRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.aif2InvestmentsRepository = aif2InvestmentsRepository;
        this.pmsAndAIFReportService = pmsAndAIFReportService;
        this.aifBlendMonthlyCalculationRepository = aifBlendMonthlyCalculationRepository;
        this.bcadDistributorFeeRepository = bcadDistributorFeeRepository;
        this.genericPayTrailUpfrontRepository = genericPayTrailUpfrontRepository;
        this.aifDistributorFeeRepository = aifDistributorFeeRepository;
        this.pmsStrategyWiseService = pmsStrategyWiseService;
        this.clientCommissionRepository = clientCommissionRepository;
        this.transactionReportRepository = transactionReportRepository;
        this.aifUmbrellaCalculationRepository = aifUmbrellaCalculationRepository;
        this.aif2SeriesMasterRepository = aif2SeriesMasterRepository;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    DateFormat firstDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public List<ReportBcadMonthlyCalculation> bcadReport(ReportGeneration reportGeneration) throws Exception {
        List<ReportBcadMonthlyCalculation> reportBcadMonthlyCalculationAdd = new ArrayList<>();
        Product product = productRepository.findByProductName("BCAD");
        List<CommissionDefinition> commissionDefinition1 = commissionDefinitionRepository.findCommissionss(reportGeneration.getDistributorMaster1().getId(), product.getId());


       /* if(commissionDefinition.getStartYear()!=null)
        {
            reportBcadMonthlyCalculationAdd = kotakCalculation(reportBcadMonthlyCalculationAdd,reportGeneration,commissionDefinition,product);
        }
        else {*/
        // Long commissionId = commissionDefinitionRepository.findDistributor(reportGeneration.getDistributorMaster1().getId());
        List<DistributorOption> distributorOptions = distributorOptionRepository.findByProduct(product);
        for (DistributorOption distributorOption : distributorOptions) {

            List<ClientManagement> clientManagementList = clientManagementRepository.findByDistributorMasterAndProductAndDistributorOption(reportGeneration.getDistributorMaster1(), product, distributorOption);

            CommissionDefinition commissionDefinition = new CommissionDefinition();

            for (ClientManagement clientManagement : clientManagementList) {
                if (clientManagement.getSlab() != null) {
                    if (clientManagement.getSlab().equals("OLD")) {
                        commissionDefinition = commissionDefinition1.stream()
                            .filter(customer -> (customer.getPmsInvest() == 0))
                            .findAny()
                            .orElse(null);

                    } else {
                        commissionDefinition = commissionDefinition1.stream()
                            .filter(customer -> (customer.getPmsInvest() == 1))
                            .findAny()
                            .orElse(null);

                    }
                }


            }


            List<ReportBcadMonthlyCalculation> reportBcadMonthlyCalculationList = new ArrayList<>();
            List<ReportBcadMonthlyCalculation> reportBcadMonthlyCalculations = new ArrayList<>();
            List<Long> idList = new ArrayList<>();


            if (clientManagementList.size() != 0)
                idList = clientManagementList.stream().map(ClientManagement::getId).collect(Collectors.toList());

            ReportBcadMonthlyCalculation reportBcadMonthlyCalculation = null;

            DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
            String sFrom = dateForm.format(reportGeneration.getStartDate());
            String sTo = dateForm.format(reportGeneration.getToDate());
            // reportBcadMonthlyCalculationRepository.findDeleteReports(sFrom, sTo, reportGeneration.getDistributorMaster1().getId());

            Calendar cal = Calendar.getInstance();
            Calendar endMonth = Calendar.getInstance();
            Calendar beginCalendar = Calendar.getInstance();
            Calendar finishCalendar = Calendar.getInstance();
            Calendar managementCalendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

            String sMonthStart = dateFormat.format(reportGeneration.getStartDate());
            String sMonthEnd = dateFormat.format(reportGeneration.getToDate());
            beginCalendar.setTime(dateFormat.parse(sMonthStart));
            finishCalendar.setTime(dateFormat.parse(sMonthEnd));
            Integer sFlag = 0;
            Integer sKotak = 0;
            Date sKotakRecent = new Date();
            Date sKotakRecentValue = new Date();

            if (commissionDefinition.getStartYear() != null) {
                sKotak = 1;
            }
            while (!beginCalendar.after(finishCalendar)) {
                sFlag = 0;
                String sStart = firstDateFormat.format(beginCalendar.getTime());
                Date sStartDate = firstDateFormat.parse(sStart);
                endMonth.setTime(sStartDate);
                endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                String sEnd = firstDateFormat.format(endMonth.getTime());
                Date sEndDate = firstDateFormat.parse(sEnd);
                List<CommissionDefinition> checkOption3 = commissionDefinitionRepository.findOption3Commission(
                    reportGeneration.getDistributorMaster1().getId(), product.getId());
                Date sOption = firstDateFormat.parse(prop.getString("option.starts"));
                Date kotakNational = firstDateFormat.parse(prop.getString("kotak.option1.national"));
                if (checkOption3 != null) {
                    if (checkOption3.size() > 0) {
                        if (sOption.before(sStartDate))
                            sFlag = 1;
                    }
                }
                if (idList.size() != 0) {
                    reportBcadMonthlyCalculations = reportBcadMonthlyCalculationRepository.findMonthlyReports(sStart, sEnd, reportGeneration.getDistributorMaster1().getId(), idList);
                    System.out.println(reportBcadMonthlyCalculations.size());
                }
                if (reportBcadMonthlyCalculations.size() == 0 && sFlag == 0) {
                    for (ClientManagement clientManagement : clientManagementList) {
                        Integer kotakOld = 0;
                        if (sKotak == 1 && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                            if (sOption.before(sStartDate)) {
                                sKotakRecentValue = reportBcadMonthlyCalculationRepository.kotakUpfront(clientManagement.getId());
                                if (kotakNational.after(sKotakRecentValue))
                                    kotakOld = 1;
                                System.out.println("Client Code :" + clientManagement.getClientCode() + "Starting Date:" + sKotakRecentValue);
                            }
                        }

                        System.out.println(clientManagement.getClientCode());

                        Double initialFundCalc = 0.0;
                        Double additionalFundCalc = 0.0;
                        Double payoutCalc = 0.0;

                        Float initialFund = bcadUpfrontMasterRepository.findInitialFund(sStart, sEnd, clientManagement.getId());
                        Float additionalFund = bcadUpfrontMasterRepository.findAdditionalFund(sStart, sEnd, clientManagement.getId());
                        Float interAcTransfers = bcadUpfrontMasterRepository.findInterAcTransfers(sStart, sEnd, clientManagement.getId());
                        Float capitalPayout = bcadUpfrontMasterRepository.findCapitalPayout(sStart, sEnd, clientManagement.getId());
                        Float profitPayout = bcadUpfrontMasterRepository.findProfitPayout(sStart, sEnd, clientManagement.getId());

                        ReportBcadMonthlyCalculation noCorpus = reportBcadMonthlyCalculationRepository.findByBeforeDate(sStart, clientManagement.getId());
                        Double carryUpfront = 0.0;
                        if ((initialFund != null && initialFund != 0.0) || (additionalFund != null && additionalFund != 0.0) || (interAcTransfers != null && interAcTransfers != 0.0)
                            || (capitalPayout != null && capitalPayout != 0.0) || (profitPayout != null && profitPayout != 0.0)) {
                            reportBcadMonthlyCalculation = new ReportBcadMonthlyCalculation();
                            reportBcadMonthlyCalculation.setClientManagement(clientManagement);
                            reportBcadMonthlyCalculation.setProduct(clientManagement.getProduct());
                            reportBcadMonthlyCalculation.setDistributorMaster(clientManagement.getDistributorMaster());
                            reportBcadMonthlyCalculation.setRelationshipManager(clientManagement.getRelationshipManager());
                            reportBcadMonthlyCalculation.setSubRM(clientManagement.getSubRM());
                            if (additionalFund != null && additionalFund != 0.0) {
                                additionalFundCalc = (double) additionalFund;
                                reportBcadMonthlyCalculation.setAdditionalCorpus((double) additionalFund);
                            } else
                                reportBcadMonthlyCalculation.setAdditionalCorpus(additionalFundCalc);

                            reportBcadMonthlyCalculation.setFromDate(beginCalendar.getTime());

                            if (initialFund != null && initialFund != 0.0) {
                                initialFundCalc += (double) initialFund;
                                reportBcadMonthlyCalculation.setOpeningCorpus((double) initialFund);
                            }
                            if (interAcTransfers != null && interAcTransfers >= 0.0) {
                                additionalFundCalc += (double) interAcTransfers;
                                reportBcadMonthlyCalculation.setAdditionalCorpus(additionalFundCalc);
                            }
                            if ((interAcTransfers != null && interAcTransfers <= 0.0)) {
                                payoutCalc += (double) interAcTransfers;
                                reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            }
                            if ((capitalPayout != null && capitalPayout <= 0.0)) {
                                payoutCalc += (double) capitalPayout;
                                reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            }
                            if ((profitPayout != null && profitPayout <= 0.0)) {
                                payoutCalc += (double) profitPayout;
                                reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            }

                            Double corpusValue = initialFundCalc + additionalFundCalc;
                            Double upfrontFee = 0.0;
                            // need to handle

                            if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                upfrontFee = (corpusValue * commissionDefinition.getUpfrontper()) / 100;

                                // cummalituve * series based percentage
                            }
                            if (noCorpus != null) {
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    carryUpfront = noCorpus.getTrialUpfrontPayable();
                                    reportBcadMonthlyCalculation.setCarryUpfront(carryUpfront);

                                    String concatDate = "";
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(beginCalendar.getTime());
                                    c.add(Calendar.MONTH, 36);
                                    //Temporary Calculation noCorpus.getThreeYear().equals("")
                                    if (noCorpus.getThreeYear() == null)
                                        concatDate = dateForm.format(c.getTime());
                                    else
                                        concatDate = noCorpus.getThreeYear() + "," + dateForm.format(c.getTime());
                                    //Temporary 3 year calculation !concatDate.equals("")
                                    if (concatDate != null) {
                                        String threeCheck = concatDate.substring(0, 10);
                                        Date threeDate = dateForm.parse(threeCheck);

                                        if (threeDate.equals(beginCalendar.getTime())) {
                                            c.setTime(beginCalendar.getTime());
                                            c.add(Calendar.MONTH, -36);
                                            ReportBcadMonthlyCalculation threeCorpus = reportBcadMonthlyCalculationRepository.findByFromDateAndClientManagement(c.getTime(), clientManagement);
                                           /* Double additionalCorpus=(threeCorpus.getAdditionalCorpus()>0) ?threeCorpus.getAdditionalCorpus():0f;
                                            Double openingCorpus=(additionalCorpus>0)? additionalCorpus :threeCorpus.getOpeningCorpus();*/
                                            Double threeadjustUpfront = (threeCorpus.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12);
                                            Double minusValue = (noCorpus.getMinusValue() == null) ? 0f : noCorpus.getMinusValue();
                                            // minusValue=minusValue -threeadjustUpfront;
                                            minusValue = -threeadjustUpfront;
                                            reportBcadMonthlyCalculation.setMinusValue(minusValue);
                                            if (concatDate.length() > 10)
                                                concatDate = concatDate.substring(11, concatDate.length());
                                            else
                                                concatDate = "";
                                        }
                                    }
                                    reportBcadMonthlyCalculation.setThreeYear(concatDate);
                                }

                                reportBcadMonthlyCalculation.setOpeningCorpus(noCorpus.getCumulativeCorpus());
                                reportBcadMonthlyCalculation.setCumulativeCorpus((double) corpusValue + noCorpus.getCumulativeCorpus() + payoutCalc);

                            } else {
                                reportBcadMonthlyCalculation.setCumulativeCorpus((double) corpusValue + payoutCalc);
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(beginCalendar.getTime());
                                    c.add(Calendar.MONTH, 36);
                                    reportBcadMonthlyCalculation.setThreeYear(dateForm.format(c.getTime()));
                                }
                            }

                            reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            reportBcadMonthlyCalculation.setUpfrontFee(upfrontFee);
                            Double managementFee = 0.0;
                            Double performanceFee = 0.0;
                            Double distributorShare = 0.0;
                            Double adjustUpfront = 0.0;
                            Double manageAdjustUpfront = 0.0;
                            Double carryForwardWithdraw = 0.0;
                            List<BCADPMSNav> calculation = bcadpmsNavRepository.findByClientManagementAndSelectedStartDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                            BCADProfitShare performanceCalc = bcadProfitShareRepository.findByClientManagementAndReceiptDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                            if (calculation.size() != 0) {
                                Float navManagement = 0f;
                                Float navComm = 0f;
                                Float profitComm = 0f;
                                for (BCADPMSNav nav : calculation) {
                                    navManagement += nav.getCalcPmsNav();
                                }
                                CommissionDefinitionOptionMap optionSelect = commissionDefinition.getCommissionDefinitionOptionMaps().stream().filter
                                    (x -> (x.getDistributorOption().getOptionName().contains(clientManagement.getDistributorOption().getOptionName()))).findAny().orElse(null);

                                ClientCommission clientCommissionOne = new ClientCommission();
                                clientCommissionOne = clientCommissionRepository.findbcadClientMaster(clientManagement.getId());

                                if (optionSelect.getFeeCalculation().equals("Management") || optionSelect.getFeeCalculation().equals("Both")) {
                                    if (managementFee == 0) {
                                        managementFee = (double) navManagement;
                                        navComm = clientCommissionOne.getNavComm() / 100;
                                        reportBcadMonthlyCalculation.setManagementFee(managementFee);
                                    }

                                }
                                if (performanceCalc != null && (optionSelect.getFeeCalculation().equals("Performance") || optionSelect.getFeeCalculation().equals("Both"))) {
                                    performanceFee = (double) performanceCalc.getProfitShareIncome();
                                    profitComm = clientCommissionOne.getProfitComm() / 100;
                                    reportBcadMonthlyCalculation.setPerformanceFee(performanceFee);
                                }
//                                    distributorShare = (managementFee + performanceFee) * distributorCommission;
                                distributorShare = (double) (navComm + profitComm);
                                reportBcadMonthlyCalculation.setDistributorShare(distributorShare);
                            }
                            if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                Double minusValue = ((reportBcadMonthlyCalculation.getMinusValue() == null) ? 0 : reportBcadMonthlyCalculation.getMinusValue());
                                adjustUpfront = ((reportBcadMonthlyCalculation.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12)) + minusValue;
                                System.out.println(adjustUpfront + "----------" + minusValue);
                                //adjustUpfront = adjustUpfront+ ((reportBcadMonthlyCalculation.getMinusValue()==null)?0d:reportBcadMonthlyCalculation.getMinusValue());

                                reportBcadMonthlyCalculation.setAdjUpfrontFee(adjustUpfront);
                                if (payoutCalc < 0.0) {
                                    adjustUpfront = 0.0;
                                    reportBcadMonthlyCalculation.setAdjUpfrontFee(adjustUpfront);
                                }
                            }

                            if (calculation.size() != 0 || performanceCalc != null) {
                                if (calculation.size() != 0 && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    Date selectedDate = reportBcadMonthlyCalculationRepository.managementDate(clientManagement.getId(), sStartDate);
                                    if (selectedDate == null) {
                                        manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeDate(clientManagement.getId(), sStart);
                                    } else {
                                        managementCalendar.setTime(firstDateFormat.parse(sStart));
                                        managementCalendar.add(Calendar.DATE, -1);
                                        String sManage = firstDateFormat.format(managementCalendar.getTime());
                                        manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeSelectedDate(clientManagement.getId(), sManage, sStart);
                                    }
                                }
                                reportBcadMonthlyCalculation.setTotalFee(managementFee + performanceFee);
                                if (manageAdjustUpfront == null) {
                                    manageAdjustUpfront = reportBcadMonthlyCalculation.getAdjUpfrontFee();
                                } else {
                                    manageAdjustUpfront += adjustUpfront;
                                }
                                reportBcadMonthlyCalculation.setNetTrialPayable(distributorShare - manageAdjustUpfront);
                            }
                            if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                if (reportBcadMonthlyCalculation.getWithdrawlAmount() < 0.0) {
                                    carryForwardWithdraw = -reportBcadMonthlyCalculation.getCarryUpfront();
                                    reportBcadMonthlyCalculation.setUpfrontWithdrawl(carryForwardWithdraw);
                                }
                                if (clientManagement.getDistributorMaster().getDistributorType().getDistTypeName().equals("National") && upfrontFee > 0.0) {
                                    Double nationalUpfront = 0.0;
                                    nationalUpfront = (upfrontFee / commissionDefinition.getUpfrontper()) * commissionDefinition.getAdjustmentper();


                                    // take upfront directly

                                    reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront + nationalUpfront - adjustUpfront + carryForwardWithdraw);

                                } else {
                                    reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront + upfrontFee - adjustUpfront + carryForwardWithdraw);

                                    // e;se part
                                }
                            }
                            reportBcadMonthlyCalculationAdd.add(reportBcadMonthlyCalculation);
                            reportBcadMonthlyCalculationRepository.save(reportBcadMonthlyCalculation);
                        } else {
                            if (noCorpus != null) {
                                reportBcadMonthlyCalculation = new ReportBcadMonthlyCalculation();
                                reportBcadMonthlyCalculation.setClientManagement(clientManagement);
                                reportBcadMonthlyCalculation.setProduct(clientManagement.getProduct());
                                reportBcadMonthlyCalculation.setDistributorMaster(clientManagement.getDistributorMaster());
                                reportBcadMonthlyCalculation.setRelationshipManager(clientManagement.getRelationshipManager());
                                reportBcadMonthlyCalculation.setSubRM(clientManagement.getSubRM());
                                reportBcadMonthlyCalculation.setAdditionalCorpus(additionalFundCalc);
                                reportBcadMonthlyCalculation.setFromDate(beginCalendar.getTime());

                                Double carryUpfronts = 0.0;
                                reportBcadMonthlyCalculation.setOpeningCorpus(noCorpus.getCumulativeCorpus());
                                reportBcadMonthlyCalculation.setCumulativeCorpus(noCorpus.getCumulativeCorpus());
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    reportBcadMonthlyCalculation.setUpfrontFee(0.0);
                                }
                                List<BCADPMSNav> calculation = bcadpmsNavRepository.findByClientManagementAndSelectedStartDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                                BCADProfitShare performanceCalc = bcadProfitShareRepository.findByClientManagementAndReceiptDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                                Double managementFee = 0.0;
                                Double performanceFee = 0.0;
                                Double distributorShare = 0.0;
                                Double adjustUpfront = 0.0;
                                Double manageAdjustUpfront = 0.0;
                                if (calculation.size() != 0) {
                                    Float navManagement = 0f;
                                    Float navComm = 0f;
                                    Float profitComm = 0f;
                                    for (BCADPMSNav nav : calculation) {
                                        navManagement += nav.getCalcPmsNav();
                                    }
                                    CommissionDefinitionOptionMap optionSelect = commissionDefinition.getCommissionDefinitionOptionMaps().stream().filter
                                        (x -> (x.getDistributorOption().getOptionName().contains(clientManagement.getDistributorOption().getOptionName()))).findAny().orElse(null);
                                    ClientCommission clientCommissionTwo = new ClientCommission();
                                    clientCommissionTwo = clientCommissionRepository.findbcadClientMaster(clientManagement.getId());
                                    if (optionSelect.getFeeCalculation().equals("Management") || optionSelect.getFeeCalculation().equals("Both")) {
                                        managementFee = (double) navManagement;
                                        navComm = clientCommissionTwo.getNavComm() / 100;
                                        reportBcadMonthlyCalculation.setManagementFee(managementFee);
                                    }
                                    if (performanceCalc != null && (optionSelect.getFeeCalculation().equals("Performance") || optionSelect.getFeeCalculation().equals("Both"))) {
                                        performanceFee = (double) performanceCalc.getProfitShareIncome();
                                        profitComm = clientCommissionTwo.getProfitComm() / 100;
                                        reportBcadMonthlyCalculation.setPerformanceFee(performanceFee);
                                    }
                                    /* Recently Commented  */
//                                        distributorShare = (managementFee + performanceFee) * distributorCommission;
                                    distributorShare = (double) (navComm + profitComm);
                                    reportBcadMonthlyCalculation.setDistributorShare(distributorShare);
                                }
                                if (noCorpus.getTrialUpfrontPayable() != null && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    carryUpfront = noCorpus.getTrialUpfrontPayable();
                                    reportBcadMonthlyCalculation.setCarryUpfront(noCorpus.getTrialUpfrontPayable());

                                }
                                if (noCorpus.getTrialUpfrontPayable() == null && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    reportBcadMonthlyCalculation.setCarryUpfront(carryUpfront);
                                }
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    String concatDate = noCorpus.getThreeYear();
                                    //Temporary for three years !concat.equals("")
                                    if (concatDate != null) {
                                        String threeCheck = concatDate.substring(0, 10);
                                        Date threeDate = dateForm.parse(threeCheck);
                                        if (threeDate.equals(beginCalendar.getTime())) {
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(beginCalendar.getTime());
                                            c.add(Calendar.MONTH, -36);
                                            ReportBcadMonthlyCalculation threeCorpus = reportBcadMonthlyCalculationRepository.findByFromDateAndClientManagement(c.getTime(), clientManagement);
                                           /* Double additionalCorpus=(threeCorpus.getAdditionalCorpus()>0) ?threeCorpus.getAdditionalCorpus():0f;
                                            Double openingCorpus=(additionalCorpus>0)? additionalCorpus :threeCorpus.getOpeningCorpus();*/
                                            Double threeadjustUpfront = (threeCorpus.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12);
                                            Double minusValue = (noCorpus.getMinusValue() == null) ? 0f : noCorpus.getMinusValue();
                                            // minusValue=minusValue -threeadjustUpfront;
                                            minusValue = -threeadjustUpfront;
                                            reportBcadMonthlyCalculation.setMinusValue(minusValue);
                                            if (concatDate.length() > 10)
                                                concatDate = concatDate.substring(11, concatDate.length());
                                            else {
                                                concatDate = "";
                                            }
                                        }
                                    }
                                    //Temporary three year noCorpus.getThreeYear().equals("")
                                    if (noCorpus.getThreeYear() == null) {
                                        reportBcadMonthlyCalculation.setMinusValue(noCorpus.getMinusValue());
                                    }
                                    reportBcadMonthlyCalculation.setThreeYear(concatDate);
                                    adjustUpfront = (noCorpus.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12);
                                    // adjustUpfront = adjustUpfront+ ((reportBcadMonthlyCalculation.getMinusValue()==null)?0:adjustUpfront);
                                    Double minusValue = ((reportBcadMonthlyCalculation.getMinusValue() == null) ? 0d : reportBcadMonthlyCalculation.getMinusValue());
                                    adjustUpfront = adjustUpfront + minusValue;
                                    reportBcadMonthlyCalculation.setAdjUpfrontFee(adjustUpfront);
                                }
                                if (calculation.size() != 0 || performanceCalc != null) {
                                    if (calculation.size() != 0 && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                        Date selectedDate = reportBcadMonthlyCalculationRepository.managementDate(clientManagement.getId(), sStartDate);
                                        if (selectedDate == null) {
                                            manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeDate(clientManagement.getId(), sStart);
                                        } else {
                                            managementCalendar.setTime(firstDateFormat.parse(sStart));
                                            managementCalendar.add(Calendar.DATE, -1);
                                            String sManage = firstDateFormat.format(managementCalendar.getTime());
                                            manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeSelectedDate(clientManagement.getId(), sManage, sStart);
                                        }
                                    }
                                    reportBcadMonthlyCalculation.setTotalFee(managementFee + performanceFee);
                                    if (manageAdjustUpfront == null) {
                                        manageAdjustUpfront = reportBcadMonthlyCalculation.getAdjUpfrontFee();
                                    } else {
                                        manageAdjustUpfront += adjustUpfront;
                                    }
                                    reportBcadMonthlyCalculation.setNetTrialPayable(distributorShare - manageAdjustUpfront);
                                }
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront + reportBcadMonthlyCalculation.getUpfrontFee() - adjustUpfront);
                                }
                                reportBcadMonthlyCalculationAdd.add(reportBcadMonthlyCalculation);
                                reportBcadMonthlyCalculationRepository.save(reportBcadMonthlyCalculation);
                            }

                        }
                    }
                } else {
                    reportBcadMonthlyCalculationAdd.addAll(reportBcadMonthlyCalculations);
                }

                BCADDistributorFee bcadDistributorFee = bcadDistributorFeeRepository.getMonthlyFee(sStart, reportGeneration.getDistributorMaster1().getId());
                BCADDistributorFee bcadDistributorFeeBefore = bcadDistributorFeeRepository.getMonthlyFeeBefore(sStart, reportGeneration.getDistributorMaster1().getId());
                Float distributorShareOption1 = 0f;
                if (bcadDistributorFee == null && distributorOption.getOptionName().equals("Option 1")) {
                    if (idList.size() != 0)
                        distributorShareOption1 = reportBcadMonthlyCalculationRepository.getDistributorShare1(sStart, reportGeneration.getDistributorMaster1().getId(), idList);

                    BCADDistributorFee bcadDistributorCalc = new BCADDistributorFee();
                    bcadDistributorCalc.setDistributorMaster(reportGeneration.getDistributorMaster1());
                    bcadDistributorCalc.setStartDate(beginCalendar.getTime());
                    if (distributorOption.getOptionName().equals("Option 1")) {
                        Float openingUpfrontBal = (bcadDistributorFeeBefore == null) ? 0f : bcadDistributorFeeBefore.getClosingBalOption1();
                        bcadDistributorCalc.setOpeningBalOption1((float) openingUpfrontBal);
                        Float genericPayTrailUpfront = genericPayTrailUpfrontRepository.getProductPaid(sStart, sEnd, product.getId(), "Upfront", reportGeneration.getDistributorMaster1().getId());
                        Float paidAmount = (genericPayTrailUpfront == null) ? 0f : genericPayTrailUpfront;
                        bcadDistributorCalc.setUpfrontPaidOption1(paidAmount);
                        Float distributorShare1 = (distributorShareOption1 == null) ? 0f : distributorShareOption1;
                        bcadDistributorCalc.setDistShareOption1(distributorShare1);
                        bcadDistributorCalc.setClosingBalOption1(openingUpfrontBal + paidAmount - distributorShare1);
                        bcadDistributorFeeRepository.save(bcadDistributorCalc);
                    }
                }
                if (distributorOption.getOptionName().equals("Option 2")) {
                    if (idList.size() != 0)
                        distributorShareOption1 = reportBcadMonthlyCalculationRepository.getDistributorShare2(sStart, reportGeneration.getDistributorMaster1().getId(), idList);

                    BCADDistributorFee bcadDistributorFee1 = bcadDistributorFeeRepository.getMonthlyFee(sStart, reportGeneration.getDistributorMaster1().getId());
                    if (bcadDistributorFee1 == null) {
                        bcadDistributorFee1 = new BCADDistributorFee();
                        bcadDistributorFee1.setDistributorMaster(reportGeneration.getDistributorMaster1());
                        bcadDistributorFee1.setStartDate(beginCalendar.getTime());
                    }
                    Float openingUpfrontBalTrail = (bcadDistributorFeeBefore == null) ? 0f : bcadDistributorFeeBefore.getTrailPayableOption2();
                    bcadDistributorFee1.setOpeningBalOption2(openingUpfrontBalTrail);
                    Float distributorShare1 = (distributorShareOption1 == null) ? 0f : distributorShareOption1;
                    bcadDistributorFee1.setTrailShareOption2(distributorShare1);
                    Float genericPayTrailUpfront = genericPayTrailUpfrontRepository.getProductPaid(sStart, sEnd, product.getId(), "Trail", reportGeneration.getDistributorMaster1().getId());
                    Float paidAmount = (genericPayTrailUpfront == null) ? 0f : genericPayTrailUpfront;
                    bcadDistributorFee1.setTrailPaidOption2(paidAmount);
                    bcadDistributorFee1.setTrailPayableOption2(openingUpfrontBalTrail + paidAmount - distributorShare1);
                    bcadDistributorFeeRepository.save(bcadDistributorFee1);
                }

                beginCalendar.add(Calendar.MONTH, 1);

            }
        }
        //}
        return reportBcadMonthlyCalculationAdd;
    }

    private List<ReportBcadMonthlyCalculation> kotakCalculation(List<ReportBcadMonthlyCalculation> reportBcadMonthlyCalculationAdd, ReportGeneration reportGeneration, CommissionDefinition commissionDefinition, Product product) throws ParseException {

        //Long commissionId = commissionDefinitionRepository.findDistributor(reportGeneration.getDistributorMaster1().getId());
        List<DistributorOption> distributorOptions = distributorOptionRepository.findByProduct(product);
        for (DistributorOption distributorOption : distributorOptions) {
            //System.out.println(commissionDefinition.toString());

            Float distributorCommission = commissionDefinition.getDistributorComm() / 100;

            List<ClientManagement> clientManagementList = clientManagementRepository.findByDistributorMasterAndProductAndDistributorOption(reportGeneration.getDistributorMaster1(), product, distributorOption);
            List<ReportBcadMonthlyCalculation> reportBcadMonthlyCalculationList = new ArrayList<>();
            List<ReportBcadMonthlyCalculation> reportBcadMonthlyCalculations = new ArrayList<>();
            List<Long> idList = new ArrayList<>();
            if (clientManagementList.size() != 0)
                idList = clientManagementList.stream().map(ClientManagement::getId).collect(Collectors.toList());

            ReportBcadMonthlyCalculation reportBcadMonthlyCalculation = null;

            DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
            String sFrom = dateForm.format(reportGeneration.getStartDate());
            String sTo = dateForm.format(reportGeneration.getToDate());
            // reportBcadMonthlyCalculationRepository.findDeleteReports(sFrom, sTo, reportGeneration.getDistributorMaster1().getId());

            Calendar cal = Calendar.getInstance();
            Calendar endMonth = Calendar.getInstance();
            Calendar beginCalendar = Calendar.getInstance();
            Calendar finishCalendar = Calendar.getInstance();
            Calendar managementCalendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

            String sMonthStart = dateFormat.format(reportGeneration.getStartDate());
            String sMonthEnd = dateFormat.format(reportGeneration.getToDate());
            beginCalendar.setTime(dateFormat.parse(sMonthStart));
            finishCalendar.setTime(dateFormat.parse(sMonthEnd));
            Integer sFlag = 0;
            Date sKotakRecent = new Date();
            Date sKotakRecentValue = new Date();

            while (!beginCalendar.after(finishCalendar)) {
                sFlag = 0;
                String sStart = firstDateFormat.format(beginCalendar.getTime());
                Date sStartDate = firstDateFormat.parse(sStart);
                endMonth.setTime(sStartDate);
                endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                String sEnd = firstDateFormat.format(endMonth.getTime());
                Date sEndDate = firstDateFormat.parse(sEnd);
                List<CommissionDefinition> checkOption3 = commissionDefinitionRepository.findOption3Commission(
                    reportGeneration.getDistributorMaster1().getId(), product.getId());
                Date sOption = firstDateFormat.parse(prop.getString("option.starts"));
                Date kotakNational = firstDateFormat.parse(prop.getString("kotak.option1.national"));
                if (checkOption3 != null) {
                    if (sOption.before(sStartDate))
                        sFlag = 1;
                }
                if (idList.size() != 0) {
                    reportBcadMonthlyCalculations = reportBcadMonthlyCalculationRepository.findMonthlyReports(sStart, sEnd, reportGeneration.getDistributorMaster1().getId(), idList);
                    System.out.println(reportBcadMonthlyCalculations.size());
                }
                if (reportBcadMonthlyCalculations.size() == 0 && sFlag == 0) {
                    for (ClientManagement clientManagement : clientManagementList) {
                        Integer kotakOld = 0;
                        if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                            // Check the loop condition date is after Mar 2019.
                            if (sOption.before(sStartDate)) {
                                // To identify the client initial investment.
                                sKotakRecent = reportBcadMonthlyCalculationRepository.kotakUpfront(clientManagement.getId());
                                sKotakRecentValue = (sKotakRecent == null) ? sStartDate : sKotakRecent;
                                // Check the client initial investment is before Apr 2019
                                if (kotakNational.after(sKotakRecentValue))
                                    kotakOld = 1;
                                System.out.println("Client Code :" + clientManagement.getClientCode() + "Starting Date:" + sKotakRecentValue);
                            }
                        }
                        System.out.println(clientManagement.getClientCode());

                        Double initialFundCalc = 0.0;
                        Double additionalFundCalc = 0.0;
                        Double payoutCalc = 0.0;

                        Float initialFund = bcadUpfrontMasterRepository.findInitialFund(sStart, sEnd, clientManagement.getId());
                        Float additionalFund = bcadUpfrontMasterRepository.findAdditionalFund(sStart, sEnd, clientManagement.getId());
                        Float interAcTransfers = bcadUpfrontMasterRepository.findInterAcTransfers(sStart, sEnd, clientManagement.getId());
                        Float capitalPayout = bcadUpfrontMasterRepository.findCapitalPayout(sStart, sEnd, clientManagement.getId());
                        Float profitPayout = bcadUpfrontMasterRepository.findProfitPayout(sStart, sEnd, clientManagement.getId());

                        ReportBcadMonthlyCalculation noCorpus = reportBcadMonthlyCalculationRepository.findByBeforeDate(sStart, clientManagement.getId());
                        Double carryUpfront = 0.0;
                        if ((initialFund != null && initialFund != 0.0) || (additionalFund != null && additionalFund != 0.0) || (interAcTransfers != null && interAcTransfers != 0.0)
                            || (capitalPayout != null && capitalPayout != 0.0) || (profitPayout != null && profitPayout != 0.0)) {
                            reportBcadMonthlyCalculation = new ReportBcadMonthlyCalculation();
                            reportBcadMonthlyCalculation.setClientManagement(clientManagement);
                            reportBcadMonthlyCalculation.setProduct(clientManagement.getProduct());
                            reportBcadMonthlyCalculation.setDistributorMaster(clientManagement.getDistributorMaster());
                            reportBcadMonthlyCalculation.setRelationshipManager(clientManagement.getRelationshipManager());
                            reportBcadMonthlyCalculation.setSubRM(clientManagement.getSubRM());
                            if (additionalFund != null && additionalFund != 0.0) {
                                additionalFundCalc = (double) additionalFund;
                                reportBcadMonthlyCalculation.setAdditionalCorpus((double) additionalFund);
                            } else
                                reportBcadMonthlyCalculation.setAdditionalCorpus(additionalFundCalc);

                            reportBcadMonthlyCalculation.setFromDate(beginCalendar.getTime());

                            if (initialFund != null && initialFund != 0.0) {
                                initialFundCalc += (double) initialFund;
                                reportBcadMonthlyCalculation.setOpeningCorpus((double) initialFund);
                            }
                            if (interAcTransfers != null && interAcTransfers >= 0.0) {
                                additionalFundCalc += (double) interAcTransfers;
                                reportBcadMonthlyCalculation.setAdditionalCorpus(additionalFundCalc);
                            }
                            if ((interAcTransfers != null && interAcTransfers <= 0.0)) {
                                payoutCalc += (double) interAcTransfers;
                                reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            }
                            if ((capitalPayout != null && capitalPayout <= 0.0)) {
                                payoutCalc += (double) capitalPayout;
                                reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            }
                            if ((profitPayout != null && profitPayout <= 0.0)) {
                                payoutCalc += (double) profitPayout;
                                reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            }

                            Double corpusValue = initialFundCalc + additionalFundCalc;
                            Double upfrontFee = 0.0;
                            if (clientManagement.getDistributorOption().getOptionName().equals("Option 1") && kotakNational.after(sStartDate)) {
                                upfrontFee = (corpusValue * commissionDefinition.getUpfrontper()) / 100;
                            }
                            if (clientManagement.getDistributorOption().getOptionName().equals("Option 1") && kotakOld == 0 && sOption.before(sStartDate)) {
                                upfrontFee = (corpusValue * commissionDefinition.getSecUpfrontper()) / 100;
                            }
                            if (noCorpus != null) {
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    carryUpfront = noCorpus.getTrialUpfrontPayable();
                                    reportBcadMonthlyCalculation.setCarryUpfront(carryUpfront);

                                    String concatDate = "";
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(beginCalendar.getTime());
                                    // Client Investment before Apr 2019
                                    if (kotakOld == 0 && kotakNational.after(sStartDate))
                                        c.add(Calendar.MONTH, 36);
                                    //New Client Investment After Apr 2019
                                    if (kotakOld == 0 && sOption.before(sStartDate))
                                        c.add(Calendar.MONTH, 12);
                                    System.out.println(c.getTime());
                                    if (noCorpus.getThreeYear().equals("") && upfrontFee != 0.0)
                                        concatDate = dateForm.format(c.getTime());
                                    else
                                        concatDate = noCorpus.getThreeYear() + "," + dateForm.format(c.getTime());
                                    if (!concatDate.equals("")) {
                                        String threeCheck = concatDate.substring(0, 10);
                                        Date threeDate = dateForm.parse(threeCheck);

                                        if (threeDate.equals(beginCalendar.getTime())) {
                                            c.setTime(beginCalendar.getTime());
                                            if (kotakOld == 0 && kotakNational.after(sStartDate))
                                                c.add(Calendar.MONTH, -36);
                                            //New Client Investment After Apr 2019
                                            if (kotakOld == 0 && sOption.before(sStartDate))
                                                c.add(Calendar.MONTH, -12);

                                            Double threeadjustUpfront = 0.0;
                                            ReportBcadMonthlyCalculation threeCorpus = reportBcadMonthlyCalculationRepository.findByFromDateAndClientManagement(c.getTime(), clientManagement);
                                            if (kotakNational.after(c.getTime())) {
                                                threeadjustUpfront = (threeCorpus.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12);
                                                Double minusValue = (noCorpus.getMinusValue() == null) ? 0f : noCorpus.getMinusValue();
                                                // minusValue=minusValue -threeadjustUpfront;
                                                minusValue = -threeadjustUpfront;
                                                reportBcadMonthlyCalculation.setMinusValue(minusValue);
                                            } else {
                                                threeadjustUpfront = threeCorpus.getUpfrontFee() / (commissionDefinition.getSecAdjustmentYr() * 12);
                                                Double minusValue = (noCorpus.getMinusValue() == null) ? 0f : noCorpus.getMinusValue();
                                                // minusValue=minusValue -threeadjustUpfront;
                                                minusValue = -threeadjustUpfront;
                                                reportBcadMonthlyCalculation.setMinusValue(minusValue);
                                            }
                                            if (concatDate.length() > 10)
                                                concatDate = concatDate.substring(11, concatDate.length());
                                            else
                                                concatDate = "";
                                        }
                                    }
                                    reportBcadMonthlyCalculation.setThreeYear(concatDate);
                                }

                                reportBcadMonthlyCalculation.setOpeningCorpus(noCorpus.getCumulativeCorpus());
                                reportBcadMonthlyCalculation.setCumulativeCorpus((double) corpusValue + noCorpus.getCumulativeCorpus() + payoutCalc);

                            } else {
                                reportBcadMonthlyCalculation.setCumulativeCorpus((double) corpusValue + payoutCalc);
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(beginCalendar.getTime());

                                    if (kotakOld == 0 && kotakNational.after(sStartDate)) {
                                        c.add(Calendar.MONTH, 36);
                                        reportBcadMonthlyCalculation.setThreeYear(dateForm.format(c.getTime()));
                                    }
                                    //New Client Investment After Apr 2019
                                    if (kotakOld == 0 && sOption.before(sStartDate)) {
                                        c.add(Calendar.MONTH, 12);
                                        reportBcadMonthlyCalculation.setThreeYear(dateForm.format(c.getTime()));
                                    }
                                }
                            }

                            reportBcadMonthlyCalculation.setWithdrawlAmount(payoutCalc);
                            reportBcadMonthlyCalculation.setUpfrontFee(upfrontFee);
                            Double managementFee = 0.0;
                            Double performanceFee = 0.0;
                            Double distributorShare = 0.0;
                            Double adjustUpfront = 0.0;
                            Double manageAdjustUpfront = 0.0;
                            Double carryForwardWithdraw = 0.0;
                            List<BCADPMSNav> calculation = bcadpmsNavRepository.findByClientManagementAndSelectedStartDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                            BCADProfitShare performanceCalc = bcadProfitShareRepository.findByClientManagementAndReceiptDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                            if (calculation.size() != 0) {
                                Float navManagement = 0f;
                                for (BCADPMSNav nav : calculation) {
                                    navManagement += nav.getCalcPmsNav();
                                }
                                CommissionDefinitionOptionMap optionSelect = commissionDefinition.getCommissionDefinitionOptionMaps().stream().filter
                                    (x -> (x.getDistributorOption().getOptionName().contains(clientManagement.getDistributorOption().getOptionName()))).findAny().orElse(null);
                                if (optionSelect.getFeeCalculation().equals("Management") || optionSelect.getFeeCalculation().equals("Both")) {
                                    if (managementFee == 0) {
                                        managementFee = (double) navManagement;
                                        reportBcadMonthlyCalculation.setManagementFee(managementFee);
                                    }

                                }
                                if (performanceCalc != null && (optionSelect.getFeeCalculation().equals("Performance") || optionSelect.getFeeCalculation().equals("Both"))) {
                                    performanceFee = (double) performanceCalc.getProfitShareIncome();
                                    reportBcadMonthlyCalculation.setPerformanceFee(performanceFee);
                                }
                                distributorShare = (managementFee + performanceFee) * distributorCommission;
                                reportBcadMonthlyCalculation.setDistributorShare(distributorShare);
                            }
                            if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                Double minusValue = ((reportBcadMonthlyCalculation.getMinusValue() == null) ? 0 : reportBcadMonthlyCalculation.getMinusValue());
                                if (kotakOld == 0 && kotakNational.after(sStartDate))
                                    adjustUpfront = ((reportBcadMonthlyCalculation.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12)) + minusValue;
                                if (kotakOld == 0 && sOption.before(sStartDate))
                                    adjustUpfront = ((reportBcadMonthlyCalculation.getCumulativeCorpus() * (commissionDefinition.getSecUpfrontper() / 100)) / (commissionDefinition.getSecAdjustmentYr() * 12)) + minusValue;
                                if (kotakOld == 1) {
                                    ReportBcadMonthlyCalculation kotakOldCorpus = reportBcadMonthlyCalculationRepository.findByFromDateAndClientManagement(kotakNational, clientManagement);
                                    adjustUpfront = ((kotakOldCorpus.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12)) + minusValue;
                                }

                                System.out.println(adjustUpfront + "----------" + minusValue);
                                //adjustUpfront = adjustUpfront+ ((reportBcadMonthlyCalculation.getMinusValue()==null)?0d:reportBcadMonthlyCalculation.getMinusValue());

                                reportBcadMonthlyCalculation.setAdjUpfrontFee(adjustUpfront);
                                if (payoutCalc < 0.0) {
                                    adjustUpfront = 0.0;
                                    reportBcadMonthlyCalculation.setAdjUpfrontFee(adjustUpfront);
                                }
                            }

                            if (calculation.size() != 0 || performanceCalc != null) {
                                if (calculation.size() != 0 && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    Date selectedDate = reportBcadMonthlyCalculationRepository.managementDate(clientManagement.getId(), sStartDate);
                                    if (selectedDate == null) {
                                        manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeDate(clientManagement.getId(), sStart);
                                    } else {
                                        managementCalendar.setTime(firstDateFormat.parse(sStart));
                                        managementCalendar.add(Calendar.DATE, -1);
                                        String sManage = firstDateFormat.format(managementCalendar.getTime());
                                        manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeSelectedDate(clientManagement.getId(), sManage, sStart);
                                    }
                                }
                                reportBcadMonthlyCalculation.setTotalFee(managementFee + performanceFee);
                                if (manageAdjustUpfront == null) {
                                    manageAdjustUpfront = reportBcadMonthlyCalculation.getAdjUpfrontFee();
                                } else {
                                    manageAdjustUpfront += adjustUpfront;
                                }
                                reportBcadMonthlyCalculation.setNetTrialPayable(distributorShare - manageAdjustUpfront);
                            }
                            if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                if (reportBcadMonthlyCalculation.getWithdrawlAmount() < 0.0) {
                                    carryForwardWithdraw = -reportBcadMonthlyCalculation.getCarryUpfront();
                                    reportBcadMonthlyCalculation.setUpfrontWithdrawl(carryForwardWithdraw);
                                }
                                if (kotakOld == 0 && upfrontFee > 0.0 && kotakNational.after(sStartDate)) {
                                    Double nationalUpfront = 0.0;
                                    nationalUpfront = (upfrontFee / commissionDefinition.getUpfrontper()) * commissionDefinition.getAdjustmentper();
                                    reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront + nationalUpfront - adjustUpfront + carryForwardWithdraw);

                                } else if (kotakOld == 0 && sOption.before(sStartDate)) {
                                    reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront + upfrontFee - adjustUpfront + carryForwardWithdraw);
                                } else if (kotakOld == 1)
                                    reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront - adjustUpfront + carryForwardWithdraw);
                                else
                                    reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront + upfrontFee - adjustUpfront + carryForwardWithdraw);

                            }
                            reportBcadMonthlyCalculationAdd.add(reportBcadMonthlyCalculation);
                            reportBcadMonthlyCalculationRepository.save(reportBcadMonthlyCalculation);
                        } else {
                            if (noCorpus != null) {
                                reportBcadMonthlyCalculation = new ReportBcadMonthlyCalculation();
                                reportBcadMonthlyCalculation.setClientManagement(clientManagement);
                                reportBcadMonthlyCalculation.setProduct(clientManagement.getProduct());
                                reportBcadMonthlyCalculation.setDistributorMaster(clientManagement.getDistributorMaster());
                                reportBcadMonthlyCalculation.setRelationshipManager(clientManagement.getRelationshipManager());
                                reportBcadMonthlyCalculation.setSubRM(clientManagement.getSubRM());
                                reportBcadMonthlyCalculation.setAdditionalCorpus(additionalFundCalc);
                                reportBcadMonthlyCalculation.setFromDate(beginCalendar.getTime());

                                Double carryUpfronts = 0.0;
                                reportBcadMonthlyCalculation.setOpeningCorpus(noCorpus.getCumulativeCorpus());
                                reportBcadMonthlyCalculation.setCumulativeCorpus(noCorpus.getCumulativeCorpus());
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    reportBcadMonthlyCalculation.setUpfrontFee(0.0);
                                }
                                List<BCADPMSNav> calculation = bcadpmsNavRepository.findByClientManagementAndSelectedStartDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                                BCADProfitShare performanceCalc = bcadProfitShareRepository.findByClientManagementAndReceiptDateBetweenAndIsDeleted(clientManagement, sStartDate, sEndDate, 0);
                                Double managementFee = 0.0;
                                Double performanceFee = 0.0;
                                Double distributorShare = 0.0;
                                Double adjustUpfront = 0.0;
                                Double manageAdjustUpfront = 0.0;
                                if (calculation.size() != 0) {
                                    Float navManagement = 0f;
                                    for (BCADPMSNav nav : calculation) {
                                        navManagement += nav.getCalcPmsNav();
                                    }
                                    CommissionDefinitionOptionMap optionSelect = commissionDefinition.getCommissionDefinitionOptionMaps().stream().filter
                                        (x -> (x.getDistributorOption().getOptionName().contains(clientManagement.getDistributorOption().getOptionName()))).findAny().orElse(null);
                                    if (optionSelect.getFeeCalculation().equals("Management") || optionSelect.getFeeCalculation().equals("Both")) {
                                        managementFee = (double) navManagement;
                                        reportBcadMonthlyCalculation.setManagementFee(managementFee);
                                    }
                                    if (performanceCalc != null && (optionSelect.getFeeCalculation().equals("Performance") || optionSelect.getFeeCalculation().equals("Both"))) {
                                        performanceFee = (double) performanceCalc.getProfitShareIncome();
                                        reportBcadMonthlyCalculation.setPerformanceFee(performanceFee);
                                    }
                                    distributorShare = (managementFee + performanceFee) * distributorCommission;
                                    reportBcadMonthlyCalculation.setDistributorShare(distributorShare);
                                }
                                if (noCorpus.getTrialUpfrontPayable() != null && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    carryUpfront = noCorpus.getTrialUpfrontPayable();
                                    reportBcadMonthlyCalculation.setCarryUpfront(noCorpus.getTrialUpfrontPayable());

                                }
                                if (noCorpus.getTrialUpfrontPayable() == null && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    reportBcadMonthlyCalculation.setCarryUpfront(carryUpfront);
                                }
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    String concatDate = noCorpus.getThreeYear();
                                    if (!concatDate.equals("")) {
                                        String threeCheck = concatDate.substring(0, 10);
                                        Date threeDate = dateForm.parse(threeCheck);
                                        if (threeDate.equals(beginCalendar.getTime())) {
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(beginCalendar.getTime());
                                            if (kotakOld == 0 && kotakNational.after(sStartDate))
                                                c.add(Calendar.MONTH, -36);
                                            //New Client Investment After Apr 2019
                                            if (kotakOld == 0 && sOption.before(sStartDate))
                                                c.add(Calendar.MONTH, -12);
                                            Double threeadjustUpfront = 0.0;
                                            ReportBcadMonthlyCalculation threeCorpus = reportBcadMonthlyCalculationRepository.findByFromDateAndClientManagement(c.getTime(), clientManagement);

                                            if (kotakNational.after(c.getTime())) {
                                                threeadjustUpfront = (threeCorpus.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12);
                                                Double minusValue = (noCorpus.getMinusValue() == null) ? 0f : noCorpus.getMinusValue();
                                                // minusValue=minusValue -threeadjustUpfront;
                                                minusValue = -threeadjustUpfront;
                                                reportBcadMonthlyCalculation.setMinusValue(minusValue);
                                            } else {
                                                threeadjustUpfront = threeCorpus.getCumulativeCorpus() * (commissionDefinition.getSecUpfrontper() / 100) / (commissionDefinition.getSecAdjustmentYr() * 12);
                                                Double minusValue = (noCorpus.getMinusValue() == null) ? 0f : noCorpus.getMinusValue();
                                                // minusValue=minusValue -threeadjustUpfront;
                                                minusValue = -threeadjustUpfront;
                                                reportBcadMonthlyCalculation.setMinusValue(minusValue);
                                            }
                                            if (concatDate.length() > 10)
                                                concatDate = concatDate.substring(11, concatDate.length());
                                            else
                                                concatDate = "";
                                        }
                                    }
                                    if (noCorpus.getThreeYear().equals("")) {
                                        reportBcadMonthlyCalculation.setMinusValue(noCorpus.getMinusValue());
                                    }
                                    reportBcadMonthlyCalculation.setThreeYear(concatDate);
                                    // adjustUpfront = adjustUpfront+ ((reportBcadMonthlyCalculation.getMinusValue()==null)?0:adjustUpfront);
                                    Double minusValue = ((reportBcadMonthlyCalculation.getMinusValue() == null) ? 0d : reportBcadMonthlyCalculation.getMinusValue());

                                    if (kotakOld == 0 && kotakNational.after(sStartDate))
                                        adjustUpfront = ((reportBcadMonthlyCalculation.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12)) + minusValue;
                                    if (kotakOld == 0 && sOption.before(sStartDate)) {
                                        adjustUpfront = ((reportBcadMonthlyCalculation.getCumulativeCorpus() * (commissionDefinition.getSecUpfrontper() / 100)) / (commissionDefinition.getSecAdjustmentYr() * 12)) + minusValue;
                                    }
                                    if (kotakOld == 1) {
                                        ReportBcadMonthlyCalculation kotakOldCorpus = reportBcadMonthlyCalculationRepository.findByFromDateAndClientManagement(kotakNational, clientManagement);
                                        adjustUpfront = ((kotakOldCorpus.getCumulativeCorpus() * (commissionDefinition.getAdjustmentper() / 100)) / (commissionDefinition.getAdjustmentyr() * 12)) + minusValue;
                                    }

                                    adjustUpfront = adjustUpfront + minusValue;
                                    reportBcadMonthlyCalculation.setAdjUpfrontFee(adjustUpfront);
                                }
                                if (calculation.size() != 0 || performanceCalc != null) {
                                    if (calculation.size() != 0 && clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                        Date selectedDate = reportBcadMonthlyCalculationRepository.managementDate(clientManagement.getId(), sStartDate);
                                        if (selectedDate == null) {
                                            manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeDate(clientManagement.getId(), sStart);
                                        } else {
                                            managementCalendar.setTime(firstDateFormat.parse(sStart));
                                            managementCalendar.add(Calendar.DATE, -1);
                                            String sManage = firstDateFormat.format(managementCalendar.getTime());
                                            manageAdjustUpfront = reportBcadMonthlyCalculationRepository.managementFeeSelectedDate(clientManagement.getId(), sManage, sStart);
                                        }
                                    }
                                    reportBcadMonthlyCalculation.setTotalFee(managementFee + performanceFee);
                                    if (manageAdjustUpfront == null) {
                                        manageAdjustUpfront = reportBcadMonthlyCalculation.getAdjUpfrontFee();
                                    } else {
                                        manageAdjustUpfront += adjustUpfront;
                                    }
                                    reportBcadMonthlyCalculation.setNetTrialPayable(distributorShare - manageAdjustUpfront);
                                }
                                if (clientManagement.getDistributorOption().getOptionName().equals("Option 1")) {
                                    if (kotakOld == 0 && sOption.before(sStartDate)) {
                                        reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront + reportBcadMonthlyCalculation.getUpfrontFee() - adjustUpfront);
                                    } else if (kotakOld == 1 || sOption.after(sStartDate))
                                        reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront - adjustUpfront);
                                    else
                                        reportBcadMonthlyCalculation.setTrialUpfrontPayable(carryUpfront - adjustUpfront);
                                }
                                reportBcadMonthlyCalculationAdd.add(reportBcadMonthlyCalculation);
                                reportBcadMonthlyCalculationRepository.save(reportBcadMonthlyCalculation);
                            }

                        }
                    }
                } else {
                    reportBcadMonthlyCalculationAdd.addAll(reportBcadMonthlyCalculations);
                }

                BCADDistributorFee bcadDistributorFee = bcadDistributorFeeRepository.getMonthlyFee(sStart, reportGeneration.getDistributorMaster1().getId());
                BCADDistributorFee bcadDistributorFeeBefore = bcadDistributorFeeRepository.getMonthlyFeeBefore(sStart, reportGeneration.getDistributorMaster1().getId());
                Float distributorShareOption1 = 0f;
                if (bcadDistributorFee == null && distributorOption.getOptionName().equals("Option 1")) {
                    if (idList.size() != 0)
                        distributorShareOption1 = reportBcadMonthlyCalculationRepository.getDistributorShare1(sStart, reportGeneration.getDistributorMaster1().getId(), idList);

                    BCADDistributorFee bcadDistributorCalc = new BCADDistributorFee();
                    bcadDistributorCalc.setDistributorMaster(reportGeneration.getDistributorMaster1());
                    bcadDistributorCalc.setStartDate(beginCalendar.getTime());
                    if (distributorOption.getOptionName().equals("Option 1")) {
                        Float openingUpfrontBal = (bcadDistributorFeeBefore == null) ? 0f : bcadDistributorFeeBefore.getClosingBalOption1();
                        bcadDistributorCalc.setOpeningBalOption1((float) openingUpfrontBal);
                        Float genericPayTrailUpfront = genericPayTrailUpfrontRepository.getProductPaid(sStart, sEnd, product.getId(), "Upfront", reportGeneration.getDistributorMaster1().getId());
                        Float paidAmount = (genericPayTrailUpfront == null) ? 0f : genericPayTrailUpfront;
                        bcadDistributorCalc.setUpfrontPaidOption1(paidAmount);
                        Float distributorShare1 = (distributorShareOption1 == null) ? 0f : distributorShareOption1;
                        bcadDistributorCalc.setDistShareOption1(distributorShare1);
                        bcadDistributorCalc.setClosingBalOption1(openingUpfrontBal + paidAmount - distributorShare1);
                        bcadDistributorFeeRepository.save(bcadDistributorCalc);
                    }
                }
                if (distributorOption.getOptionName().equals("Option 2")) {
                    if (idList.size() != 0)
                        distributorShareOption1 = reportBcadMonthlyCalculationRepository.getDistributorShare2(sStart, reportGeneration.getDistributorMaster1().getId(), idList);

                    BCADDistributorFee bcadDistributorFee1 = bcadDistributorFeeRepository.getMonthlyFee(sStart, reportGeneration.getDistributorMaster1().getId());
                    if (bcadDistributorFee1 == null) {
                        bcadDistributorFee1 = new BCADDistributorFee();
                        bcadDistributorFee1.setDistributorMaster(reportGeneration.getDistributorMaster1());
                        bcadDistributorFee1.setStartDate(beginCalendar.getTime());
                    }
                    Float openingUpfrontBalTrail = (bcadDistributorFeeBefore == null) ? 0f : bcadDistributorFeeBefore.getTrailPayableOption2();
                    bcadDistributorFee1.setOpeningBalOption2(openingUpfrontBalTrail);
                    Float distributorShare1 = (distributorShareOption1 == null) ? 0f : distributorShareOption1;
                    bcadDistributorFee1.setTrailShareOption2(distributorShare1);
                    Float genericPayTrailUpfront = genericPayTrailUpfrontRepository.getProductPaid(sStart, sEnd, product.getId(), "Trail", reportGeneration.getDistributorMaster1().getId());
                    Float paidAmount = (genericPayTrailUpfront == null) ? 0f : genericPayTrailUpfront;
                    bcadDistributorFee1.setTrailPaidOption2(paidAmount);
                    bcadDistributorFee1.setTrailPayableOption2(openingUpfrontBalTrail + paidAmount - distributorShare1);
                    bcadDistributorFeeRepository.save(bcadDistributorFee1);
                }

                beginCalendar.add(Calendar.MONTH, 1);

            }
        }
        return reportBcadMonthlyCalculationAdd;
    }

    FileOutputStream fos = null;
    HSSFWorkbook workBook = null;

    public List<String> generateReports(List<ReportBcadMonthlyCalculation> distValue, List<AIF2MonthlyCalculation> aif2DistValue, List<AIFBlendMonthlyCalculation> aifBlendDistValue, List<AIFUmbrella> aifUmbrellas, DistributorMaster dm, ReportGeneration reportGeneration, List<String> fileDownload) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
            String sStartTime = dateFormat.format(reportGeneration.getStartDate());
            String sEndTime = dateFormat.format(reportGeneration.getToDate());
            String sFinal = sStartTime + "_to_" + sEndTime;
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            Product product = productRepository.findByProductName("BCAD");
            List<CommissionDefinition> commissionDefinition1 = commissionDefinitionRepository.findCommissionss(dm.getId(), product.getId());
            CommissionDefinition commissionDefinition= new CommissionDefinition();
            if(commissionDefinition1.size()!=0) {
                 commissionDefinition = commissionDefinition1.get(0);
            }
            Double distributorShare = 0.0, distributorShare1 = 0.0, distributorShare2 = 0.0, trailShare = 0.0, upfrontShare = 0.0,
                carryForward = 0.0;
            Double managementFee1 = 0.0, managementFee2 = 0.0, totalFee1 = 0.0, totalFee2 = 0.0, adjacentAgainst = 0.0, netTrailPayable1 = 0.0,
                netTrailPayable2 = 0.0, carryOverUpfront = 0.0, adjacentSummary = 0.0;
            ReportBcadMonthlyCalculation reportBcadMonthlyCalculation = new ReportBcadMonthlyCalculation();
            CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD = new CumulativeAIFSeriesBCAD();
            String filePath = "";
            String file = "";
            if (reportGeneration.getRelationManage() == null) {
                filePath = prop.getString("fee.file.folder") + "DFA Backup\\" +
                    prop.getString("file.bcad.output") + "\\" + sFinal;
                workBook = new HSSFWorkbook();
                File dirFiles = new File(filePath);
                dirFiles.mkdirs();
                file = filePath + "/" + dm.getDistName() + " " + sFinal + ".xls";
                fileDownload.add(file);
            }

            if (reportGeneration.getRelationManage() != null) {
                filePath = prop.getString("fee.file.folder") + "DFA Backup\\" +
                    prop.getString("file.bcad.output") + "\\" + prop.getString("rm.generate.folder") + "\\" + reportGeneration.getRelationManage().getRmName();
                workBook = new HSSFWorkbook();
                File dirFiles = new File(filePath);
                dirFiles.mkdirs();
                fileDownload.add(filePath);
                file = filePath + "\\\\" + dm.getDistName() + " " + sFinal + ".xls";
            }

            fos = new FileOutputStream(file);

            HSSFSheet sheetSummary = workBook.createSheet("Summary");
            HSSFSheet sheetPMS = workBook.createSheet("PMS");
            HSSFSheet sheetAIF = workBook.createSheet("AIF");
            HSSFSheet sheet = workBook.createSheet("BCAD");
            HSSFSheet sheet1 = workBook.createSheet("AIF2");
            HSSFSheet sheet2 = workBook.createSheet("AIF Blend");
            HSSFSheet sheet3 = workBook.createSheet("AIF Umbrella");


            sheet.setZoom(90);
            sheet.setDisplayGridlines(false);
            sheet1.setZoom(90);
            sheet1.setDisplayGridlines(false);
            sheet2.setZoom(90);
            sheet2.setDisplayGridlines(false);
            sheet3.setZoom(90);
            sheet3.setDisplayGridlines(false);
            sheetSummary.setZoom(90);
            sheetSummary.setDisplayGridlines(false);
            sheetPMS.setZoom(90);
            sheetPMS.setDisplayGridlines(false);
            sheetAIF.setZoom(90);
            sheetAIF.setDisplayGridlines(false);

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csRightLeftRight = workBook.createCellStyle();
            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();
            CellStyle csHorVerDate = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPercNoBorder = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");
            fFont.setBold(true);
            cs.setFont(fFont);

            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csHorVerDate.setAlignment(HorizontalAlignment.CENTER);
            csHorVerDate.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerDate.setFont(defaultFont);
            csHorVerDate.setWrapText(true);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle cellStyle = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cellStyle;

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csPlain = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);

            csPlain.setBorderLeft(BorderStyle.THIN);
            csPlain.setBorderRight(BorderStyle.THIN);

            csDFBold.setBorderLeft(BorderStyle.THIN);
            csDFBold.setBorderRight(BorderStyle.THIN);

            csPercNoBorder = csPerc;

            csPerc.setBorderLeft(BorderStyle.THIN);
            csPerc.setBorderRight(BorderStyle.THIN);

            csPercNoBorder.setBorderLeft(BorderStyle.NONE);
            csPercNoBorder.setBorderRight(BorderStyle.NONE);

            csRight.setFont(defaultFont);
            //csHorVerCenter.setFont(defaultFont);
            csDF.setFont(defaultFont);
            csPlain.setFont(defaultFont);
            //csDFBold.setFont(defaultFont);
            csPerc.setFont(defaultFont);

            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            int iRowNo = sheet.getLastRowNum() + 1;

            HSSFRow row = sheet.createRow(iRowNo);
            HSSFRow rowUserName = sheet.createRow(sheet.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(dm.getDistName().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

            HSSFRow durationFrom = sheet.createRow(sheet.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + "-" + sEndTime);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs
            // Format sheet
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            sheet.createFreezePane(0, 7);

            iRowNo = sheet.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
            // Format sheet
            headingBRSBookRow.setHeightInPoints(30);

            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            Cell cell = row.createCell(0);
            row = sheet.createRow(iRowNo);
            headingBRSBookRow.createCell(1).setCellValue("Client Code");
            headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(0).setCellValue("Client Name");
            headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
                    /*headingBRSBookRow.createCell(2).setCellValue("Distributor Name");
                    headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);*/
            headingBRSBookRow.createCell(2).setCellValue("Distributor Type");
            headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(3).setCellValue("Distributor Option");
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(4).setCellValue("RM");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(5).setCellValue("Sub RM");
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(6).setCellValue("Month");
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(7).setCellValue("Opening Corpus");
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(8).setCellValue("Additional Corpus");
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(9).setCellValue("Withdrawal");
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(10).setCellValue("Cumulative Corpus");
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(11).setCellValue("Carry Forward Upfront fee");
            headingBRSBookRow.getCell(11).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(12).setCellValue("Upfront Fee 1.5%");
            headingBRSBookRow.getCell(12).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(13).setCellValue("Management Fee");
            headingBRSBookRow.getCell(13).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(14).setCellValue("Brokerage");
            headingBRSBookRow.getCell(14).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(15).setCellValue("Performance Fee");
            headingBRSBookRow.getCell(15).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(16).setCellValue("Total Fee");
            headingBRSBookRow.getCell(16).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(17).setCellValue("Distributor Share  - " + commissionDefinition.getDistributorComm());
            headingBRSBookRow.getCell(17).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(18).setCellValue("Adjustment Against Upfront Fee");
            headingBRSBookRow.getCell(18).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(19).setCellValue("Net Trail Fee Payable to Distributor");
            headingBRSBookRow.getCell(19).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(20).setCellValue("Upfront Adj for Withdrawal");
            headingBRSBookRow.getCell(20).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(21).setCellValue("Carry over of Upfront Fee");
            headingBRSBookRow.getCell(21).setCellStyle(csHorVerCenter);
            // List<ClientManagement> clientManagementList =clientManagementRepository.findByDistributorMasterAndDistributorOption(reportGeneration.getDistributorMaster1());
            sheet.setDefaultColumnWidth(12);
            sheet.autoSizeColumn(0);

            String monthSeperation = "";
            String optionChange = "";
            String checkOptions = "";
            if (distValue.size() != 0) {
                checkOptions = distValue.get(0).getClientManagement().getDistributorOption().getOptionName();
            }
            //sheet.autoSizeColumn(7);
            // sheet.setColumnWidth(7, 0);
                   /*for(ClientManagement cm:clientManagementList)
                    {
                        List<ReportBcadMonthlyCalculation> clientReports = distValue.stream().filter(o -> (o.getClientManagement().getClientCode().equals(cm.getClientCode())))
                            .collect(Collectors.toList());*/
//                        if(clientReports.size()!=0) {
            for (ReportBcadMonthlyCalculation report : distValue) {
                optionChange = report.getClientManagement().getDistributorOption().getOptionName();

                if (!optionChange.equals(checkOptions)) {
                    checkOptions = report.getClientManagement().getDistributorOption().getOptionName();


                    CellStyle csFour = cs;
                    CellStyle csDFBoldFour = csDFBold;

                    csFour.setBorderBottom(BorderStyle.THIN);
                    csFour.setBorderTop(BorderStyle.THIN);
                    csFour.setBorderLeft(BorderStyle.THIN);
                    csFour.setBorderRight(BorderStyle.THIN);

                    csDFBoldFour.setBorderBottom(BorderStyle.THIN);
                    csDFBoldFour.setBorderTop(BorderStyle.THIN);
                    csDFBoldFour.setBorderLeft(BorderStyle.THIN);
                    csDFBoldFour.setBorderRight(BorderStyle.THIN);

                    HSSFRow rowTotal = sheet.createRow(sheet.getLastRowNum() + 1);
                    rowTotal.createCell(0).setCellValue("TOTAL");
                    rowTotal.getCell(0).setCellStyle(csFour);

                    HSSFCell cellTotal = rowTotal.createCell(1);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(2);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(3);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(4);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(5);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(6);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(7);
                    cellTotal.setCellValue("");//sPms
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(8);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(9);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(10);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(11);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(12);
                    cellTotal.setCellValue(upfrontShare.floatValue());
                    cellTotal.setCellType(CellType.NUMERIC);
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(13);
                    cellTotal.setCellValue((managementFee1.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(14);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(15);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(16);
                    cellTotal.setCellValue(totalFee1.floatValue());
                    cellTotal.setCellType(CellType.NUMERIC);
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(17);
                    cellTotal.setCellValue((distributorShare1.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(18);
                    cellTotal.setCellValue((adjacentAgainst.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(19);
                    cellTotal.setCellValue((netTrailPayable1.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                    cellTotal.setCellStyle(csDFBoldFour);

                    cellTotal = rowTotal.createCell(20);
                    cellTotal.setCellValue("");
                    cellTotal.setCellStyle(csDFBoldFour);


                    cellTotal = rowTotal.createCell(21);
                    cellTotal.setCellValue((carryOverUpfront.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                    cellTotal.setCellStyle(csDFBoldFour);

                }

                if (monthSeperation.equals("") || (!monthSeperation.equals(dateFormat.format(report.getFromDate())))) {
                    monthSeperation = dateFormat.format(report.getFromDate());
                    iRowNo = sheet.getLastRowNum() + 1;
                    row = sheet.createRow(iRowNo);
                    sheet.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 20));
                    cell = row.createCell(0);
                    cell.setCellValue(monthSeperation);
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(1);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(2);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(3);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(4);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(5);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(6);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(7);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(8);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(9);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(10);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(11);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(12);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(13);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(14);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(15);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(16);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(17);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(18);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(19);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);
                    cell = row.createCell(20);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);
                    cell = row.createCell(21);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                }

                iRowNo = sheet.getLastRowNum() + 1;
                row = sheet.createRow(iRowNo);

                cell = row.createCell(1);
                cell.setCellValue(report.getClientManagement().getClientCode());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(0);
                cell.setCellValue(report.getClientManagement().getClientName());
                cell.setCellStyle(cellStyle);
                sheet.autoSizeColumn(0);

                cell = row.createCell(2);
                cell.setCellValue(report.getClientManagement().getDistributorMaster().getDistributorType().getDistTypeName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(3);
                cell.setCellValue(report.getClientManagement().getDistributorOption().getOptionName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(4);
                if (report.getClientManagement().getRelationshipManager() != null)
                    cell.setCellValue(report.getClientManagement().getRelationshipManager().getRmName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(5);
                if (report.getClientManagement().getSubRM() != null)
                    cell.setCellValue(report.getClientManagement().getSubRM().getSubName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(6);
                String reportDate = dateFormat.format(report.getFromDate());
                cell.setCellValue(reportDate);
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(7);
                if (report.getOpeningCorpus() != null)
                    cell.setCellValue(report.getOpeningCorpus());
                else {
                    cell.setCellValue(0);
                }
                cell.setCellStyle(csDF);

                cell = row.createCell(8);
                if (report.getAdditionalCorpus() != null)
                    cell.setCellValue(report.getAdditionalCorpus());
                cell.setCellStyle(csDF);

                cell = row.createCell(9);
                if (report.getWithdrawlAmount() != null && report.getWithdrawlAmount() < 0.0)
                    cell.setCellValue(report.getWithdrawlAmount());
                cell.setCellStyle(csDF);

                cell = row.createCell(10);
                if (report.getCumulativeCorpus() != null)
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getCumulativeCorpus())));
                cell.setCellStyle(csDF);

                cell = row.createCell(11);
                if (report.getCarryUpfront() != null)
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getCarryUpfront())));
                if (sStartTime.equals(reportDate) && report.getCarryUpfront() != null)
                    carryForward += Double.parseDouble(decimalFormat.format(report.getCarryUpfront()));
                cell.setCellStyle(csDF);

                cell = row.createCell(12);
                if (report.getUpfrontFee() != null) {
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getUpfrontFee())));
                    upfrontShare += Double.parseDouble(decimalFormat.format(report.getUpfrontFee()));
                }
                cell.setCellStyle(cellStyle);

                cell = row.createCell(13);
                if (report.getManagementFee() != null) {
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getManagementFee())));
                    if (report.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                        managementFee1 += Double.parseDouble(decimalFormat.format(report.getManagementFee()));
                    else
                        managementFee2 += Double.parseDouble(decimalFormat.format(report.getManagementFee()));
                }
                cell.setCellStyle(csDF);

                cell = row.createCell(14);
                cell.setCellStyle(csDF);

                cell = row.createCell(15);
                if (report.getPerformanceFee() != null)
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getPerformanceFee())));
                cell.setCellStyle(csDF);

                cell = row.createCell(16);
                if (report.getTotalFee() != null) {
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getTotalFee())));
                    if (report.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                        totalFee1 += Double.parseDouble(decimalFormat.format(report.getTotalFee()));
                    else
                        totalFee2 += Double.parseDouble(decimalFormat.format(report.getTotalFee()));
                }
                cell.setCellStyle(csDF);

                cell = row.createCell(17);
                if (report.getDistributorShare() != null) {
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getDistributorShare())));
                    distributorShare += Double.parseDouble(decimalFormat.format(report.getDistributorShare()));
                    if (report.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                        distributorShare1 += Double.parseDouble(decimalFormat.format(report.getDistributorShare()));
                    else
                        distributorShare2 += Double.parseDouble(decimalFormat.format(report.getDistributorShare()));

                }
                cell.setCellStyle(csDF);


                cell = row.createCell(18);
                if (report.getAdjUpfrontFee() != null)
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getAdjUpfrontFee())));
                if (report.getClientManagement().getDistributorOption().getOptionName().equals("Option 1")) {
                    adjacentAgainst += Double.parseDouble(decimalFormat.format(report.getAdjUpfrontFee()));
                }
                if (report.getClientManagement().getDistributorOption().getOptionName().equals("Option 1") &&
                    report.getManagementFee() != null) {
                    adjacentSummary += Double.parseDouble(decimalFormat.format(report.getAdjUpfrontFee()));
                }

                cell.setCellStyle(csDF);

                cell = row.createCell(19);
                if (report.getNetTrialPayable() != null) {
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getNetTrialPayable())));
                    trailShare += Double.parseDouble(decimalFormat.format(report.getNetTrialPayable()));
                    if (report.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                        netTrailPayable1 += Double.parseDouble(decimalFormat.format(report.getNetTrialPayable()));
                    else
                        netTrailPayable2 += Double.parseDouble(decimalFormat.format(report.getNetTrialPayable()));
                }
                cell.setCellStyle(csDF);

                cell = row.createCell(20);
                if (report.getUpfrontWithdrawl() != null)
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getUpfrontWithdrawl())));
                cell.setCellStyle(csDF);

                cell = row.createCell(21);
                if (report.getTrialUpfrontPayable() != null) {
                    cell.setCellValue(Double.parseDouble(decimalFormat.format(report.getTrialUpfrontPayable())));
                    if (sEndTime.equals(dateFormat.format(report.getFromDate())) && report.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                        carryOverUpfront += Double.parseDouble(decimalFormat.format(report.getTrialUpfrontPayable()));
                }
                cell.setCellStyle(csDF);


            }
            //}
            iRowNo++;
            //}

            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);
            ReportBcadMonthlyCalculation totalReports = new ReportBcadMonthlyCalculation();
            if (distValue.size() != 0)
                totalReports = distValue.get(distValue.size() - 1);

            HSSFRow rowTotal = sheet.createRow(sheet.getLastRowNum() + 1);
            rowTotal.createCell(0).setCellValue("TOTAL");
            rowTotal.getCell(0).setCellStyle(csFour);

            HSSFCell cellTotal = rowTotal.createCell(1);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(2);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(3);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(4);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(5);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(6);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(7);
            cellTotal.setCellValue("");//sPms
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(8);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(9);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(10);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(11);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(12);
            if (distValue.size() != 0) {
                if (totalReports.getClientManagement().getDistributorOption().getOptionName().equals("Option 1")) {
                    cellTotal.setCellValue((upfrontShare.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                }
            } else
                cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(13);
            if (distValue.size() != 0) {
                if (totalReports.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                    cellTotal.setCellValue((managementFee1.floatValue()));
                else
                    cellTotal.setCellValue((managementFee2.floatValue()));
                cellTotal.setCellType(CellType.NUMERIC);
            }

            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(14);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(15);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(16);
            if (distValue.size() != 0) {
                if (totalReports.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                    cellTotal.setCellValue((totalFee1.floatValue()));
                else
                    cellTotal.setCellValue((totalFee2.floatValue()));
                cellTotal.setCellType(CellType.NUMERIC);
            }
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(17);
            if (distValue.size() != 0) {
                if (totalReports.getClientManagement().getDistributorOption().getOptionName().equals("Option 1"))
                    cellTotal.setCellValue((distributorShare1.floatValue()));
                else
                    cellTotal.setCellValue((distributorShare2.floatValue()));
                cellTotal.setCellType(CellType.NUMERIC);
            }
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(18);
            if (distValue.size() != 0) {
                if (totalReports.getClientManagement().getDistributorOption().getOptionName().equals("Option 1")) {
                    cellTotal.setCellValue((adjacentAgainst.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                } else
                    cellTotal.setCellValue("");
            }
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(19);
            if (distValue.size() != 0) {
                if (totalReports.getClientManagement().getDistributorOption().getOptionName().equals("Option 1")) {
                    cellTotal.setCellValue((netTrailPayable1.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                } else
                    cellTotal.setCellValue((netTrailPayable2.floatValue()));
                cellTotal.setCellType(CellType.NUMERIC);
            }

            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(20);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);
            cellTotal = rowTotal.createCell(21);
            if (distValue.size() != 0) {
                if (totalReports.getClientManagement().getDistributorOption().getOptionName().equals("Option 1")) {
                    cellTotal.setCellValue((carryOverUpfront.floatValue()));
                    cellTotal.setCellType(CellType.NUMERIC);
                }
            }
            cellTotal.setCellStyle(csDFBoldFour);

            cumulativeAIFSeriesBCAD.setBCADDistShare(prop.getString("bcad.distributor.share"));
            cumulativeAIFSeriesBCAD.setBCADDistValue(distributorShare);
            cumulativeAIFSeriesBCAD.setBCADTrailName(prop.getString("bcad.trail.share"));
            cumulativeAIFSeriesBCAD.setBCADTrailValue(trailShare);
            cumulativeAIFSeriesBCAD.setBCADUpfrontName(prop.getString("bcad.upfront.share"));
            cumulativeAIFSeriesBCAD.setBCADUpfrontValue(upfrontShare);
            cumulativeAIFSeriesBCAD.setBCADDistOption1(netTrailPayable1);
            cumulativeAIFSeriesBCAD.setBCADDistOption2(netTrailPayable2);
            cumulativeAIFSeriesBCAD.setDistributorShare1(distributorShare1);
            cumulativeAIFSeriesBCAD.setDistributorShare2(distributorShare2);
            cumulativeAIFSeriesBCAD.setAgainstUpfrontFee(adjacentSummary);
            cumulativeAIFSeriesBCAD.setCarryOverForward(carryForward);
            cumulativeAIFSeriesBCAD.setAgainstUpfrontOriginal(adjacentAgainst);

            aif2ReportGeneration(aif2DistValue, dm, reportGeneration, sheet1, sStartTime, sEndTime, cumulativeAIFSeriesBCAD);
            aifBlendReportGeneration(aifBlendDistValue, dm, reportGeneration, sheet2, sStartTime, sEndTime, cumulativeAIFSeriesBCAD);

            aif2UmbrellaReportGeneration(aifUmbrellas, dm, reportGeneration, sheet3, sStartTime, sEndTime, cumulativeAIFSeriesBCAD);



            /*PMS Reports*/
            // pmsAndAIFReportService.generateDistributorReports(dm, reportGeneration,sheetSummary,sheetPMS,sheetAIF,sStartTime,sEndTime,workBook,cumulativeAIFSeriesBCAD);
            /*Strategy Wise Reports*/
            pmsStrategyWiseService.generateDistributorReports(dm, reportGeneration, sheetSummary, sheetPMS, sheetAIF, sStartTime, sEndTime, workBook, cumulativeAIFSeriesBCAD);
            workBook.write(fos);


        } catch (Exception e) {
            System.out.println(e);

        } finally {
            try {
                fos.flush();
            } catch (IOException e) {
                System.out.println(e);

            }
            try {
                fos.close();
            } catch (IOException e) {

            }
            try {
                workBook.close();

            } catch (IOException e) {
                System.out.println(e);


            }
        }
        return fileDownload;
    }

    private void aifBlendReportGeneration(List<AIFBlendMonthlyCalculation> aifBlendDistValue, DistributorMaster dm, ReportGeneration reportGeneration, HSSFSheet sheet2, String sStartTime, String sEndTime, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(dm.getDistName());

        Product product = productRepository.findByProductName("AIF Blend");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(distributorMaster.getId(), product.getId());

        Double aifBlendTotal = 0.0;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        HSSFFont defaultFont = workBook.createFont();
        defaultFont.setFontHeightInPoints((short) 11);
        defaultFont.setFontName("Calibri");

        CellStyle cs = workBook.createCellStyle();
        CellStyle csRight = workBook.createCellStyle();
        CellStyle csRightLeftRight = workBook.createCellStyle();
        // Format sheet
        CellStyle csHorVerCenter = workBook.createCellStyle();
        CellStyle csHorVerDate = workBook.createCellStyle();
        CellStyle csPlanLeftRight = workBook.createCellStyle();
        CellStyle csPlainNoBorder = workBook.createCellStyle();
        CellStyle csPercNoBorder = workBook.createCellStyle();

        HSSFFont fFont = workBook.createFont();
        fFont.setFontHeightInPoints((short) 11);
        fFont.setFontName("Calibri");
        fFont.setBold(true);
        cs.setFont(fFont);

        HSSFFont fFontNoBold = workBook.createFont();
        fFontNoBold.setFontHeightInPoints((short) 11);
        fFontNoBold.setFontName("Calibri");

        csRight.setAlignment(HorizontalAlignment.LEFT);
        // Format sheet
        csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
        csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        csHorVerCenter.setFont(fFont);
        csHorVerCenter.setWrapText(true);

        csHorVerCenter.setBorderTop(BorderStyle.THIN);
        csHorVerCenter.setBorderBottom(BorderStyle.THIN);
        csHorVerCenter.setBorderLeft(BorderStyle.THIN);
        csHorVerCenter.setBorderRight(BorderStyle.THIN);

        csHorVerDate.setAlignment(HorizontalAlignment.CENTER);
        csHorVerDate.setVerticalAlignment(VerticalAlignment.CENTER);
        csHorVerDate.setFont(defaultFont);
        csHorVerDate.setWrapText(true);

        csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
        csPlanLeftRight.setBorderRight(BorderStyle.THIN);
        csPlanLeftRight.setFont(fFontNoBold);

        CellStyle cellStyle = workBook.createCellStyle();
        CellStyle csMergedCellHeader = cellStyle;

        CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

        CellStyle csPlain = StyleUtil.getStyleDataFormat(workBook);

        CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

        CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

        csDF.setBorderLeft(BorderStyle.THIN);
        csDF.setBorderRight(BorderStyle.THIN);

        csPlain.setBorderLeft(BorderStyle.THIN);
        csPlain.setBorderRight(BorderStyle.THIN);

        csDFBold.setBorderLeft(BorderStyle.THIN);
        csDFBold.setBorderRight(BorderStyle.THIN);

        csPercNoBorder = csPerc;

        csPerc.setBorderLeft(BorderStyle.THIN);
        csPerc.setBorderRight(BorderStyle.THIN);

        csPercNoBorder.setBorderLeft(BorderStyle.NONE);
        csPercNoBorder.setBorderRight(BorderStyle.NONE);

        csRight.setFont(defaultFont);
        //csHorVerCenter.setFont(defaultFont);
        csDF.setFont(defaultFont);
        csPlain.setFont(defaultFont);
        //csDFBold.setFont(defaultFont);
        csPerc.setFont(defaultFont);

        csPlainNoBorder.setFont(fFont);
        csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
        csPlainNoBorder.setBorderTop(BorderStyle.NONE);
        csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
        csPlainNoBorder.setBorderRight(BorderStyle.NONE);

        int iRowNo = sheet2.getLastRowNum() + 1;

        HSSFRow row = sheet2.createRow(iRowNo);
        HSSFRow rowUserName = sheet2.createRow(sheet2.getLastRowNum() + 1);
        rowUserName.createCell(0).setCellValue(dm.getDistName().toUpperCase());
        rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

        HSSFRow durationFrom = sheet2.createRow(sheet2.getLastRowNum() + 1);
        durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + "-" + sEndTime);
        durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs
        // Format sheet
        sheet2.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
        sheet2.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

        int iDistCommLineNum = sheet2.getLastRowNum() + 1;
        boolean bIsCommissionSet = false;
        HSSFRow distComm = sheet2.createRow(iDistCommLineNum);
        distComm.createCell(0).setCellValue("Distributor Commission");
        distComm.getCell(0).setCellStyle(csPlainNoBorder);
        if (commissionDefinition != null) {
            distComm.createCell(1).setCellValue(commissionDefinition.getDistributorComm() / 100);
            distComm.getCell(1).setCellStyle(csPercNoBorder);
            distComm.getCell(1).setCellType(CellType.NUMERIC);
        }


        sheet2.createFreezePane(0, 8);

        iRowNo = sheet2.getLastRowNum() + 3;
        HSSFRow headingBRSBookRow = sheet2.createRow(iRowNo);
        // Format sheet
        headingBRSBookRow.setHeightInPoints(30);

        sheet2.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

        Cell cell = row.createCell(0);
        row = sheet2.createRow(iRowNo);
        headingBRSBookRow.createCell(1).setCellValue("Client Code");
        headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(0).setCellValue("Client Name");
        headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(2).setCellValue("Series Name");
        headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(3).setCellValue("RM");
        headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(4).setCellValue("Sub RM");
        headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(5).setCellValue("Subscription Amount");
        headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(6).setCellValue("Unit Price");
        headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(7).setCellValue("No. of Units");
        headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(8).setCellValue("Month");
        headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(9).setCellValue("Management Fee");
        headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(10).setCellValue("Amount to Distributor");
        headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);

        sheet2.setDefaultColumnWidth(12);
        sheet2.autoSizeColumn(0);

        String monthSeperation = "";

        for (AIFBlendMonthlyCalculation report : aifBlendDistValue) {

            if (monthSeperation.equals("") || (!monthSeperation.equals(dateFormat.format(report.getFromDate())))) {
                monthSeperation = dateFormat.format(report.getFromDate());
                iRowNo = sheet2.getLastRowNum() + 1;
                row = sheet2.createRow(iRowNo);
                sheet2.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 10));
                cell = row.createCell(0);
                cell.setCellValue(monthSeperation);
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(1);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(2);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(3);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(4);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(5);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(6);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(7);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(8);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(9);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(10);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

            }

            iRowNo = sheet2.getLastRowNum() + 1;
            row = sheet2.createRow(iRowNo);

            cell = row.createCell(1);
            cell.setCellValue(report.getClientManagement().getClientCode());
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(0);
            cell.setCellValue(report.getClientManagement().getClientName());
            cell.setCellStyle(cellStyle);
            sheet2.autoSizeColumn(0);

            cell = row.createCell(2);
            cell.setCellValue(report.getAif2SeriesMaster().getClassType());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            if (report.getClientManagement().getRelationshipManager() != null)
                cell.setCellValue(report.getClientManagement().getRelationshipManager().getRmName());
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(4);
            if (report.getClientManagement().getSubRM() != null)
                cell.setCellValue(report.getClientManagement().getSubRM().getSubName());
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(5);
            cell.setCellValue(report.getTotOfUnits());
            cell.setCellStyle(csDF);

            cell = row.createCell(6);
            cell.setCellValue(report.getUnitPrice());
            cell.setCellStyle(csDF);

            cell = row.createCell(7);
            cell.setCellValue(report.getNoOfUnits());
            cell.setCellStyle(csDF);

            cell = row.createCell(8);
            String reportDate = dateFormat.format(report.getFromDate());
            cell.setCellValue(reportDate);
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(9);
            cell.setCellValue(report.getManagementFee());
            cell.setCellStyle(csDF);

            cell = row.createCell(10);
            cell.setCellValue(report.getDistShare());
            aifBlendTotal += report.getDistShare();
            cell.setCellStyle(csDF);

        }
        //}
        iRowNo++;
        //}

        CellStyle csFour = cs;
        CellStyle csDFBoldFour = csDFBold;

        csFour.setBorderBottom(BorderStyle.THIN);
        csFour.setBorderTop(BorderStyle.THIN);
        csFour.setBorderLeft(BorderStyle.THIN);
        csFour.setBorderRight(BorderStyle.THIN);

        csDFBoldFour.setBorderBottom(BorderStyle.THIN);
        csDFBoldFour.setBorderTop(BorderStyle.THIN);
        csDFBoldFour.setBorderLeft(BorderStyle.THIN);
        csDFBoldFour.setBorderRight(BorderStyle.THIN);

        HSSFRow rowTotal = sheet2.createRow(sheet2.getLastRowNum() + 1);
        rowTotal.createCell(0).setCellValue("Total");
        rowTotal.getCell(0).setCellStyle(csFour);

        HSSFCell cellTotal = rowTotal.createCell(1);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(2);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(3);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(4);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(5);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(6);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(7);
        cellTotal.setCellValue("");//sPms
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(8);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(9);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(10);
        cellTotal.setCellValue((aifBlendTotal.floatValue()));
        cellTotal.setCellType(CellType.NUMERIC);
        cellTotal.setCellStyle(csDFBoldFour);

        cumulativeAIFSeriesBCAD.setAIFBlendName(prop.getString("aif.blend.total"));
        cumulativeAIFSeriesBCAD.setAIFBlendValue(aifBlendTotal);

    }

    private void aif2ReportGeneration(List<AIF2MonthlyCalculation> aif2DistValue, DistributorMaster dm, ReportGeneration reportGeneration, HSSFSheet sheet1, String sStartTime, String sEndTime, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(dm.getDistName());
        Product product = productRepository.findByProductName("AIF2");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(dm.getId(), product.getId());
        Double aif2Toal = 0.0;

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        HSSFFont defaultFont = workBook.createFont();
        defaultFont.setFontHeightInPoints((short) 11);
        defaultFont.setFontName("Calibri");

        CellStyle cs = workBook.createCellStyle();
        CellStyle csRight = workBook.createCellStyle();
        CellStyle csRightLeftRight = workBook.createCellStyle();
        // Format sheet
        CellStyle csHorVerCenter = workBook.createCellStyle();
        CellStyle csHorVerDate = workBook.createCellStyle();
        CellStyle csPlanLeftRight = workBook.createCellStyle();
        CellStyle csPlainNoBorder = workBook.createCellStyle();
        CellStyle csPercNoBorder = workBook.createCellStyle();

        HSSFFont fFont = workBook.createFont();
        fFont.setFontHeightInPoints((short) 11);
        fFont.setFontName("Calibri");
        fFont.setBold(true);
        cs.setFont(fFont);

        HSSFFont fFontNoBold = workBook.createFont();
        fFontNoBold.setFontHeightInPoints((short) 11);
        fFontNoBold.setFontName("Calibri");

        csRight.setAlignment(HorizontalAlignment.LEFT);
        // Format sheet
        csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
        csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        csHorVerCenter.setFont(fFont);
        csHorVerCenter.setWrapText(true);

        csHorVerCenter.setBorderTop(BorderStyle.THIN);
        csHorVerCenter.setBorderBottom(BorderStyle.THIN);
        csHorVerCenter.setBorderLeft(BorderStyle.THIN);
        csHorVerCenter.setBorderRight(BorderStyle.THIN);

        csHorVerDate.setAlignment(HorizontalAlignment.CENTER);
        csHorVerDate.setVerticalAlignment(VerticalAlignment.CENTER);
        csHorVerDate.setFont(defaultFont);
        csHorVerDate.setWrapText(true);

        csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
        csPlanLeftRight.setBorderRight(BorderStyle.THIN);
        csPlanLeftRight.setFont(fFontNoBold);

        CellStyle cellStyle = workBook.createCellStyle();
        CellStyle csMergedCellHeader = cellStyle;

        CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

        CellStyle csPlain = StyleUtil.getStyleDataFormat(workBook);

        CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

        CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

        csDF.setBorderLeft(BorderStyle.THIN);
        csDF.setBorderRight(BorderStyle.THIN);

        csPlain.setBorderLeft(BorderStyle.THIN);
        csPlain.setBorderRight(BorderStyle.THIN);

        csDFBold.setBorderLeft(BorderStyle.THIN);
        csDFBold.setBorderRight(BorderStyle.THIN);

        csPercNoBorder = csPerc;

        csPerc.setBorderLeft(BorderStyle.THIN);
        csPerc.setBorderRight(BorderStyle.THIN);

        csPercNoBorder.setBorderLeft(BorderStyle.NONE);
        csPercNoBorder.setBorderRight(BorderStyle.NONE);

        csRight.setFont(defaultFont);
        //csHorVerCenter.setFont(defaultFont);
        csDF.setFont(defaultFont);
        csPlain.setFont(defaultFont);
        //csDFBold.setFont(defaultFont);
        csPerc.setFont(defaultFont);


        csPlainNoBorder.setFont(fFont);
        csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
        csPlainNoBorder.setBorderTop(BorderStyle.NONE);
        csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
        csPlainNoBorder.setBorderRight(BorderStyle.NONE);

        int iRowNo = sheet1.getLastRowNum() + 1;

        HSSFRow row = sheet1.createRow(iRowNo);
        HSSFRow rowUserName = sheet1.createRow(sheet1.getLastRowNum() + 1);
        rowUserName.createCell(0).setCellValue(dm.getDistName().toUpperCase());
        rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

        HSSFRow durationFrom = sheet1.createRow(sheet1.getLastRowNum() + 1);
        durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + "-" + sEndTime);
        durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs
        // Format sheet
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
        sheet1.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

        int iDistCommLineNum = sheet1.getLastRowNum() + 1;
        boolean bIsCommissionSet = false;
        HSSFRow distComm = sheet1.createRow(iDistCommLineNum);
        distComm.createCell(0).setCellValue("Distributor Commission");
        distComm.getCell(0).setCellStyle(csPlainNoBorder);

        if (commissionDefinition != null) {
            distComm.createCell(1).setCellValue(commissionDefinition.getDistributorComm() / 100);
            distComm.getCell(1).setCellStyle(csPercNoBorder);
            distComm.getCell(1).setCellType(CellType.NUMERIC);
        }


        sheet1.createFreezePane(0, 8);

        iRowNo = sheet1.getLastRowNum() + 3;
        HSSFRow headingBRSBookRow = sheet1.createRow(iRowNo);
        // Format sheet
        headingBRSBookRow.setHeightInPoints(30);

        sheet1.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

        Cell cell = row.createCell(0);
        row = sheet1.createRow(iRowNo);
        headingBRSBookRow.createCell(1).setCellValue("Client Code");
        headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(0).setCellValue("Client Name");
        headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(2).setCellValue("Series Name");
        headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(3).setCellValue("RM");
        headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(4).setCellValue("Sub RM");
        headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(5).setCellValue("Subscription Amount");
        headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(6).setCellValue("Unit Price");
        headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(7).setCellValue("No. of Units");
        headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(8).setCellValue("Month");
        headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(9).setCellValue("Management Fee");
        headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(10).setCellValue("Amount to Distributor");
        headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);

        sheet1.setDefaultColumnWidth(12);
        sheet1.autoSizeColumn(0);

        String monthSeperation = "";

        for (AIF2MonthlyCalculation report : aif2DistValue) {

            if (monthSeperation.equals("") || (!monthSeperation.equals(dateFormat.format(report.getFromDate())))) {
                monthSeperation = dateFormat.format(report.getFromDate());
                iRowNo = sheet1.getLastRowNum() + 1;
                row = sheet1.createRow(iRowNo);
                sheet1.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 10));
                cell = row.createCell(0);
                cell.setCellValue(monthSeperation);
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(1);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(2);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(3);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(4);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(5);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(6);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(7);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(8);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(9);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

                cell = row.createCell(10);
                cell.setCellValue("");
                cell.setCellStyle(csHorVerCenter);

            }

            iRowNo = sheet1.getLastRowNum() + 1;
            row = sheet1.createRow(iRowNo);

            cell = row.createCell(1);
            cell.setCellValue(report.getClientManagement().getClientCode());
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(0);
            cell.setCellValue(report.getClientManagement().getClientName());
            cell.setCellStyle(cellStyle);
            sheet1.autoSizeColumn(0);

            cell = row.createCell(2);
            cell.setCellValue(report.getAif2SeriesMaster().getClassType());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            if (report.getClientManagement().getRelationshipManager() != null)
                cell.setCellValue(report.getClientManagement().getRelationshipManager().getRmName());
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(4);
            if (report.getClientManagement().getSubRM() != null)
                cell.setCellValue(report.getClientManagement().getSubRM().getSubName());
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(5);
            cell.setCellValue(report.getTotOfUnits());
            cell.setCellStyle(csDF);

            cell = row.createCell(6);
            cell.setCellValue(report.getUnitPrice());
            cell.setCellStyle(csDF);

            cell = row.createCell(7);
            cell.setCellValue(report.getNoOfUnits());
            cell.setCellStyle(csDF);

            cell = row.createCell(8);
            String reportDate = dateFormat.format(report.getFromDate());
            cell.setCellValue(reportDate);
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(9);
            cell.setCellValue(report.getManagementFee());
            cell.setCellStyle(csDF);

            cell = row.createCell(10);
            cell.setCellValue(report.getDistShare());
            aif2Toal += report.getDistShare();
            cell.setCellStyle(csDF);

        }
        //}
        iRowNo++;
        //}

        CellStyle csFour = cs;
        CellStyle csDFBoldFour = csDFBold;

        csFour.setBorderBottom(BorderStyle.THIN);
        csFour.setBorderTop(BorderStyle.THIN);
        csFour.setBorderLeft(BorderStyle.THIN);
        csFour.setBorderRight(BorderStyle.THIN);

        csDFBoldFour.setBorderBottom(BorderStyle.THIN);
        csDFBoldFour.setBorderTop(BorderStyle.THIN);
        csDFBoldFour.setBorderLeft(BorderStyle.THIN);
        csDFBoldFour.setBorderRight(BorderStyle.THIN);

        HSSFRow rowTotal = sheet1.createRow(sheet1.getLastRowNum() + 1);
        rowTotal.createCell(0).setCellValue("Total");
        rowTotal.getCell(0).setCellStyle(csFour);

        HSSFCell cellTotal = rowTotal.createCell(1);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(2);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(3);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(4);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(5);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(6);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(7);
        cellTotal.setCellValue("");//sPms
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(8);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(9);
        cellTotal.setCellValue("");
        cellTotal.setCellStyle(csDFBoldFour);

        cellTotal = rowTotal.createCell(10);
        cellTotal.setCellValue((aif2Toal.floatValue()));
        cellTotal.setCellType(CellType.NUMERIC);
        cellTotal.setCellStyle(csDFBoldFour);

        cumulativeAIFSeriesBCAD.setAIFGreenName(prop.getString("aif2.total.fee"));
        cumulativeAIFSeriesBCAD.setAIFGreenValue(aif2Toal);

    }

    public void compressZip(List<String> fileDownload, ReportGeneration reportGeneration) throws IOException {
        String filePath = "";
        String file = "";
        filePath = prop.getString("fee.file.folder") + "DFA Backup\\" +
            prop.getString("file.bcad.zipfolder");
        FileUtils.deleteDirectory(new File(filePath));
        workBook = new HSSFWorkbook();
        File dirFiles = new File(filePath);
        System.out.println("filesss" + dirFiles);
        //dirFiles.delete();
        dirFiles.mkdirs();
        filePath = filePath + "\\" + "FileDownload.zip";

        FileOutputStream fos = new FileOutputStream(filePath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : fileDownload) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();

        fos.close();
    }

    public void DirectoryZip(List<String> fileDownload, ReportGeneration reportGeneration) throws IOException {
        System.out.println("print" + fileDownload);
        String filePath = "";
        String file = "";
        filePath = prop.getString("fee.file.folder") + "DFA Backup\\" +
            prop.getString("file.bcad.rmfolder") + "\\" + prop.getString("rm.generate.folder");
        workBook = new HSSFWorkbook();
        File dirFiles = new File(filePath);
        System.out.println("filen" + dirFiles);
        dirFiles.delete();
        dirFiles.mkdirs();
        filePath = filePath + "\\" + "rm.zip";
        System.out.println("managers" + filePath);
        FileOutputStream fos = new FileOutputStream(filePath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String folderZip : fileDownload) {
            File fileToZip = new File(folderZip);
            System.out.println("compress" + fileDownload);
            zipFile(fileToZip, fileToZip.getName(), zipOut);
        }
        zipOut.close();
        fos.close();
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        System.out.println("new" + fileToZip);
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public List<AIF2MonthlyCalculation> aif2Report(ReportGeneration reportGeneration) throws ParseException {
        AIFDistributorFee aifDistributorFee = new AIFDistributorFee();
        List<AIF2MonthlyCalculation> aif2MonthlyCalculations = new ArrayList<>();
        AIF2MonthlyCalculation aif2MonthlyCalculation = new AIF2MonthlyCalculation();
        Product product = productRepository.findByProductName("AIF2");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(reportGeneration.getDistributorMaster1().getId(), product.getId());
        List<ClientManagement> aif2ClientManagement = clientManagementRepository.findByDistributorMasterAndProduct(reportGeneration.getDistributorMaster1(), product);
        //DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(reportGeneration.getDistributorMaster1().getDistName());

        DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
        String sFrom = dateForm.format(reportGeneration.getStartDate());
        String sTo = dateForm.format(reportGeneration.getToDate());

        Calendar cal = Calendar.getInstance();
        Calendar endMonth = Calendar.getInstance();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

        String sMonthStart = dateFormat.format(reportGeneration.getStartDate());
        String sMonthEnd = dateFormat.format(reportGeneration.getToDate());
        beginCalendar.setTime(dateFormat.parse(sMonthStart));
        finishCalendar.setTime(dateFormat.parse(sMonthEnd));

        AIFDistributorFee aifDistributorFeeBefore = aifDistributorFeeRepository.getMonthsCalcultionBefore(product.getId(), reportGeneration.getDistributorMaster1().getId(),
            sFrom);

       /*AIF2MonthlyCalculation aif2MonthlyCalculation1 =aif2MonthlyCalculationRepository.getMonthBefore(product.getId(),reportGeneration.getDistributorMaster1().getId(),
            monthlyBefore);

*/
        while (!beginCalendar.after(finishCalendar)) {
            aifDistributorFee = new AIFDistributorFee();
            String sStart = firstDateFormat.format(beginCalendar.getTime());
            Date sStartDate = firstDateFormat.parse(sStart);
            endMonth.setTime(sStartDate);
            endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
            String sEnd = firstDateFormat.format(endMonth.getTime());
            Date sEndDate = firstDateFormat.parse(sEnd);

            String monthlyDate = dateForm.format(beginCalendar.getTime());

            for (ClientManagement clientManagement : aif2ClientManagement) {
                aif2MonthlyCalculation = new AIF2MonthlyCalculation();
                AIF2Investments aif2Investments = aif2InvestmentsRepository.findByClientManagementAndProduct(clientManagement, product);
                AIF2MonthlyCalculation aif2MonthlyCalculationss = aif2MonthlyCalculationRepository.findAIF2Client(sStart, sEnd, clientManagement.getId());
                if (aif2MonthlyCalculationss == null) {
                    AIF2ManagementFee aif2ManagementFee = aif2ManagementFeeRepository.findAIF2Management(sStart, sEnd, aif2Investments.getAif2SeriesMaster().getId(), product.getId());
                    if (aif2ManagementFee != null) {
                        aif2MonthlyCalculation.setNoOfUnits(aif2Investments.getNoOfUnits());
                        Float seriesUnits = aif2InvestmentsRepository.findSeriesUnits(aif2Investments.getAif2SeriesMaster().getId(), product.getId());
                        Float managementFee = (aif2Investments.getNoOfUnits() / seriesUnits) * aif2ManagementFee.getUnits();
                        aif2MonthlyCalculation.setAif2SeriesMaster(aif2Investments.getAif2SeriesMaster());
                        aif2MonthlyCalculation.setTotOfUnits(aif2Investments.getTotOfUnits());
                        aif2MonthlyCalculation.setUnitPrice(aif2Investments.getUnitPrice());
                        aif2MonthlyCalculation.setManagementFee(managementFee);
                        aif2MonthlyCalculation.setDistShare((managementFee * commissionDefinition.getDistributorComm()) / 100);
                        aif2MonthlyCalculation.setClientManagement(clientManagement);
                        aif2MonthlyCalculation.setDistributorMaster(clientManagement.getDistributorMaster());
                        aif2MonthlyCalculation.setRelationshipManager(clientManagement.getRelationshipManager());
                        aif2MonthlyCalculation.setSubRM(clientManagement.getSubRM());
                        aif2MonthlyCalculation.setFromDate(sStartDate);
                        aif2MonthlyCalculationRepository.save(aif2MonthlyCalculation);
                        aif2MonthlyCalculations.add(aif2MonthlyCalculation);
                    }
                } else {
                    aif2MonthlyCalculations.add(aif2MonthlyCalculationss);
                }


            }
            AIFDistributorFee aifDistributorFeeCurrent = aifDistributorFeeRepository.getMonthsCalculation(product.getId(), reportGeneration.getDistributorMaster1().getId(),
                monthlyDate);
            if (aifDistributorFeeCurrent == null) {
                aifDistributorFee.setProduct(product);
                aifDistributorFee.setDistributorMaster(reportGeneration.getDistributorMaster1());
                aifDistributorFee.setStartDate(beginCalendar.getTime());
                if (aifDistributorFeeBefore == null)
                    aifDistributorFee.setOpeningBalFee(0f);
                else
                    aifDistributorFee.setOpeningBalFee(aifDistributorFeeBefore.getClosingBal());

                Float distributorShare = aif2MonthlyCalculationRepository.getDistShare(reportGeneration.getDistributorMaster1().getId(),
                    monthlyDate);
                if (distributorShare != null)
                    aifDistributorFee.setDistributorShare(distributorShare);
                else
                    aifDistributorFee.setDistributorShare(0f);
                Float genericPayTrailUpfront = genericPayTrailUpfrontRepository.getProductPaid(sStart, sEnd, product.getId(), "Trail", reportGeneration.getDistributorMaster1().getId());
                Float paidAmount = (genericPayTrailUpfront == null) ? 0f : genericPayTrailUpfront;
                aifDistributorFee.setPaidAmt(paidAmount);
                aifDistributorFee.setClosingBal(aifDistributorFee.getOpeningBalFee() + aifDistributorFee.getDistributorShare() - paidAmount);
                aifDistributorFeeRepository.save(aifDistributorFee);

            }
            beginCalendar.add(Calendar.MONTH, 1);
        }

        return aif2MonthlyCalculations;
    }

    public List<AIFBlendMonthlyCalculation> aifBlendReport(ReportGeneration reportGeneration) throws ParseException {
        List<AIFBlendMonthlyCalculation> aifBlendMonthlyCalculations = new ArrayList<>();
        AIFBlendMonthlyCalculation aifBlendMonthlyCalculation = new AIFBlendMonthlyCalculation();
        AIFDistributorFee aifBlendMonthly = new AIFDistributorFee();
        Product product = productRepository.findByProductName("AIF Blend");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(reportGeneration.getDistributorMaster1().getId(), product.getId());

        List<ClientManagement> aif2ClientManagement = clientManagementRepository.findByDistributorMasterAndProduct(reportGeneration.getDistributorMaster1(), product);
        // DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(reportGeneration.getDistributorMaster1().getDistName());

        DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
        String sFrom = dateForm.format(reportGeneration.getStartDate());
        String sTo = dateForm.format(reportGeneration.getToDate());

        Calendar cal = Calendar.getInstance();
        Calendar endMonth = Calendar.getInstance();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

        String sMonthStart = dateFormat.format(reportGeneration.getStartDate());
        String sMonthEnd = dateFormat.format(reportGeneration.getToDate());
        beginCalendar.setTime(dateFormat.parse(sMonthStart));
        finishCalendar.setTime(dateFormat.parse(sMonthEnd));

        while (!beginCalendar.after(finishCalendar)) {
            aifBlendMonthly = new AIFDistributorFee();
            String sStart = firstDateFormat.format(beginCalendar.getTime());
            Date sStartDate = firstDateFormat.parse(sStart);
            endMonth.setTime(sStartDate);
            endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
            String sEnd = firstDateFormat.format(endMonth.getTime());
            Date sEndDate = firstDateFormat.parse(sEnd);
            AIFDistributorFee aifDistributorFeeBefore = aifDistributorFeeRepository.getMonthsCalcultionBefore(product.getId(), reportGeneration.getDistributorMaster1().getId(),
                sStart);

            for (ClientManagement clientManagement : aif2ClientManagement) {
                aifBlendMonthlyCalculation = new AIFBlendMonthlyCalculation();
                AIF2Investments aif2Investments = aif2InvestmentsRepository.findByClientManagementAndProduct(clientManagement, product);
                AIFBlendMonthlyCalculation aifBlendMonthlyCalculation1 = aifBlendMonthlyCalculationRepository.findAIFBlendClient(sStart, sEnd, clientManagement.getId());
                if (aifBlendMonthlyCalculation1 == null) {
                    AIF2ManagementFee aif2ManagementFee = aif2ManagementFeeRepository.findAIF2Management(sStart, sEnd, aif2Investments.getAif2SeriesMaster().getId(), product.getId());
                    if (aif2ManagementFee != null) {
                        aifBlendMonthlyCalculation.setNoOfUnits(aif2Investments.getNoOfUnits());
                        Float seriesUnits = aif2InvestmentsRepository.findSeriesUnits(aif2Investments.getAif2SeriesMaster().getId(), product.getId());
                        Float managementFee = (aif2Investments.getNoOfUnits() / seriesUnits) * aif2ManagementFee.getUnits();
                        aifBlendMonthlyCalculation.setAif2SeriesMaster(aif2Investments.getAif2SeriesMaster());
                        aifBlendMonthlyCalculation.setTotOfUnits(aif2Investments.getTotOfUnits());
                        aifBlendMonthlyCalculation.setUnitPrice(aif2Investments.getUnitPrice());
                        aifBlendMonthlyCalculation.setManagementFee(managementFee);
                        aifBlendMonthlyCalculation.setDistShare((managementFee * commissionDefinition.getDistributorComm()) / 100);
                        aifBlendMonthlyCalculation.setClientManagement(clientManagement);
                        aifBlendMonthlyCalculation.setDistributorMaster(clientManagement.getDistributorMaster());
                        aifBlendMonthlyCalculation.setRelationshipManager(clientManagement.getRelationshipManager());
                        aifBlendMonthlyCalculation.setSubRM(clientManagement.getSubRM());
                        aifBlendMonthlyCalculation.setFromDate(sStartDate);
                        aifBlendMonthlyCalculationRepository.save(aifBlendMonthlyCalculation);
                        aifBlendMonthlyCalculations.add(aifBlendMonthlyCalculation);
                    }
                } else {
                    aifBlendMonthlyCalculations.add(aifBlendMonthlyCalculation1);
                }


            }
            AIFDistributorFee aifDistributorCurrent = aifDistributorFeeRepository.getMonthsCalculation(product.getId(), reportGeneration.getDistributorMaster1().getId(),
                sStart);
            if (aifDistributorCurrent == null) {
                aifBlendMonthly.setDistributorMaster(reportGeneration.getDistributorMaster1());
                aifBlendMonthly.setProduct(product);
                if (aifDistributorFeeBefore != null)
                    aifBlendMonthly.setOpeningBalFee(aifDistributorFeeBefore.getClosingBal());
                else
                    aifBlendMonthly.setOpeningBalFee(0f);
                Float genericPayUpfront = genericPayTrailUpfrontRepository.getProductPaid(sStart, sEnd, product.getId(), "Upfront", reportGeneration.getDistributorMaster1().getId());
                Float genericPayTrail = genericPayTrailUpfrontRepository.getProductPaid(sStart, sEnd, product.getId(), "Trail", reportGeneration.getDistributorMaster1().getId());
                genericPayTrail = (genericPayTrail == null) ? 0f : genericPayTrail;
                genericPayUpfront = (genericPayUpfront == null) ? 0f : genericPayUpfront;
                //  aifBlendMonthly.setUpfrontFee(genericPayUpfront);
                aifBlendMonthly.setPaidAmt(genericPayTrail + genericPayUpfront);

                Float distributorShare = aifBlendMonthlyCalculationRepository.getDistributorShare(sStart, sEnd, reportGeneration.getDistributorMaster1().getId());
                distributorShare = (distributorShare == null) ? 0f : distributorShare;
                aifBlendMonthly.setDistributorShare(distributorShare);
                aifBlendMonthly.setClosingBal(aifBlendMonthly.getOpeningBalFee() + distributorShare - (genericPayTrail + genericPayUpfront));
                aifBlendMonthly.setStartDate(beginCalendar.getTime());
                aifDistributorFeeRepository.save(aifBlendMonthly);

            }

            beginCalendar.add(Calendar.MONTH, 1);
        }

        return aifBlendMonthlyCalculations;
    }

    public List<AIFUmbrella> aifUmbrella2Report(ReportGeneration reportGeneration) throws ParseException {


        AIFDistributorFee aifDistributorFee = new AIFDistributorFee();
        List<AIFUmbrella> aif2MonthlyCalculations = new ArrayList<>();
//        AIF2MonthlyCalculation aif2MonthlyCalculation = new AIF2MonthlyCalculation();
        Product product = productRepository.findByProductName("UNIFI AIF Umbrella Blend Fund - 2");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(reportGeneration.getDistributorMaster1().getId(), product.getId());
        List<ClientManagement> aif2ClientManagement = clientManagementRepository.findByDistributorMasterAndProduct(reportGeneration.getDistributorMaster1(), product);
        //DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(reportGeneration.getDistributorMaster1().getDistName());

        DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
        String sFrom = dateForm.format(reportGeneration.getStartDate());
        String sTo = dateForm.format(reportGeneration.getToDate());

        Calendar cal = Calendar.getInstance();
        Calendar endMonth = Calendar.getInstance();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

        String sMonthStart = dateFormat.format(reportGeneration.getStartDate());
        String sMonthEnd = dateFormat.format(reportGeneration.getToDate());
        beginCalendar.setTime(dateFormat.parse(sMonthStart));
        finishCalendar.setTime(dateFormat.parse(sMonthEnd));

//        AIFDistributorFee aifDistributorFeeBefore= aifDistributorFeeRepository.getMonthsCalcultionBefore(product.getId(),reportGeneration.getDistributorMaster1().getId(),
//            sFrom);

       /*AIF2MonthlyCalculation aif2MonthlyCalculation1 =aif2MonthlyCalculationRepository.getMonthBefore(product.getId(),reportGeneration.getDistributorMaster1().getId(),
            monthlyBefore);

*/
        while (!beginCalendar.after(finishCalendar)) {
            aifDistributorFee = new AIFDistributorFee();
            String sStart = firstDateFormat.format(beginCalendar.getTime());
            Date sStartDate = firstDateFormat.parse(sStart);
            endMonth.setTime(sStartDate);
            endMonth.set(Calendar.DAY_OF_MONTH, endMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
            String sEnd = firstDateFormat.format(endMonth.getTime());
            Date sEndDate = firstDateFormat.parse(sEnd);

            String monthlyDate = dateForm.format(beginCalendar.getTime());
            System.out.println("client mana******" + aif2ClientManagement.size());

            for (ClientManagement clientManagement : aif2ClientManagement) {
                List<AIF2Investments> aif2Investments = aif2InvestmentsRepository.getByClientManagementAndProduct(clientManagement.getClientId(), product.getId());
                List<AIFUmbrella> aif2MonthlyCalculationss = aifUmbrellaCalculationRepository.findAIFUmbrellaClient(sStart, sEnd, clientManagement.getId());
                if (aif2Investments != null) {
                    System.out.println("inside aif client");
                    System.out.println("client id" + clientManagement.getClientId());
                    List<TransactionReport> transactionReportMonthcheck = transactionReportRepository.findMonthlyCheck(sStart);
                    if (transactionReportMonthcheck.size() != 0) {

                        List<TransactionReport> transactionReportSeries = transactionReportRepository.findSeriesIdBasedOnClient(clientManagement.getClientId(), sStart);
                        if (transactionReportSeries.size() != 0) {

                            for (TransactionReport transactionReportSeries1 : transactionReportSeries) {

                                if (aif2MonthlyCalculationss.size() == 0) {
                                    System.out.println("aif " + aif2MonthlyCalculationss);

                                    List<TransactionReport> transactionReportList = transactionReportRepository.findUmbrellaManagement(sEnd, transactionReportSeries1.getWsAccountCode());
                                    if (transactionReportList.size() > 0) {

                                        System.out.println("tran ****************" + transactionReportList.size());

                                        AIFUmbrella aifUmbrella = new AIFUmbrella();

                                        aifUmbrella.setClientManagement(clientManagement);
                                        aifUmbrella.setDistributorMaster(clientManagement.getDistributorMaster());
                                        aifUmbrella.setRelationshipManager(clientManagement.getRelationshipManager());
                                        aifUmbrella.setSubRM(clientManagement.getSubRM());

                                        aifUmbrella.setFromDate(sStartDate);
                                        Float noOfUnits = 0f;
                                        Double totalNumberUnits = 0d;
                                        Float managementfee = 0f;
                                        Float unitPrice = 0f;
                                        AIF2ManagementFee aif2ManagementFee = aif2ManagementFeeRepository.findAIF2Management(sStart, sEnd, transactionReportSeries1.getSeriesId(), product.getId());


                                        for (TransactionReport transactionReportList1 : transactionReportList) {

                                            Float seriesUnits = aif2InvestmentsRepository.findSeriesUnits(transactionReportList1.getSeriesId(), product.getId());
                                            AIF2SeriesMaster aif2SeriesMasterId = aif2SeriesMasterRepository.findById(transactionReportList1.getSeriesId()).get();
                                            aifUmbrella.setAif2SeriesMaster(aif2SeriesMasterId);

                                            if (transactionReportList1 != null) {
                                                noOfUnits = transactionReportList1.getQuantity();
                                                totalNumberUnits= transactionReportList1.getNetAmount();
                                                unitPrice = transactionReportList1.getRate();
                                                managementfee = (transactionReportList1.getQuantity() / seriesUnits) * aif2ManagementFee.getUnits();
//                                Float managementFee = (aif2Investments.getNoOfUnits() / seriesUnits) * aif2ManagementFee.getUnits();


                                            }


                                        }
                                        aifUmbrella.setNoOfUnits(noOfUnits);
                                        aifUmbrella.setTotOfUnits(totalNumberUnits);

                                        if (commissionDefinition.getTrailUpfrontOption().equalsIgnoreCase("T")) {
                                            AIFUmbrella aif2InvestmentsBeforeMonth = aifUmbrellaCalculationRepository.findByBeforeDateUmbrellas(sStart, clientManagement.getId());
                                            if (aif2InvestmentsBeforeMonth == null) {
                                                System.out.println("empty");

                                            }
                                            else {
                                                if (Double.compare(aif2InvestmentsBeforeMonth.getTotOfUnits(), totalNumberUnits) != 0) {

                                                    System.out.println("inside save");

                                                    aif2InvestmentsBeforeMonth.setFromDate(sStartDate);
                                                    aifUmbrellaCalculationRepository.save(aif2InvestmentsBeforeMonth);
                                                    aif2MonthlyCalculations.add(aif2InvestmentsBeforeMonth);



                                                }
                                                else
                                                {
                                                    aifUmbrella.setTotOfUnits(totalNumberUnits);
                                                }



                                            }


                                        }
                                        aifUmbrella.setManagementFee(managementfee);
                                        aifUmbrella.setDistShare((managementfee * commissionDefinition.getDistributorComm()) / 100);
                                        aifUmbrella.setUnitPrice(unitPrice);

                                        if (commissionDefinition.getTrailUpfrontOption().equalsIgnoreCase("U")) {

                                            Double adjustUpfront=0.0;
                                            Double manageAdjustUpfront=0.0;
                                            AIFUmbrella aif2InvestmentsBeforeMonth = aifUmbrellaCalculationRepository.findByBeforeDateUmbrellas(sStart, clientManagement.getId());
                                            if (aif2InvestmentsBeforeMonth == null) {
                                                aifUmbrella.setAdditionalCorpus(totalNumberUnits);
                                                aifUmbrella.setOpeningCorpus(0.0);
                                                Double cumulativeCorpus= aifUmbrella.getAdditionalCorpus()+aifUmbrella.getOpeningCorpus();
                                                aifUmbrella.setCumulativeCorpus(cumulativeCorpus);

                                                double percentageCalc=0d;
                                                double upfrontFee=0d;
                                                double carryForwardWithdraw=0d;
                                                System.out.println("class series"+aifUmbrella.getAif2SeriesMaster().getClassType());
                                                if(aifUmbrella.getAif2SeriesMaster().getClassType().contains("Class B1")) {
                                                     percentageCalc = totalNumberUnits / 100 * 1.50;
//                                                     adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 1.50 / 100));
                                                    adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                    aifUmbrella.setUpFront(percentageCalc);


                                                    upfrontFee = (aifUmbrella.getCumulativeCorpus() * 1.50) / 100;
//                                                    aifUmbrella.setCarryForward(aifUmbrella.getCarryOver()+ adjustUpfront);
                                                    aifUmbrella.setCarryForward(0.0);
                                                    aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);





                                                }
                                                else {

                                                     percentageCalc = totalNumberUnits / 100 * 2;
//                                                     adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 2 / 100));
                                                    adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                    aifUmbrella.setUpFront(percentageCalc);


                                                    upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                    aifUmbrella.setCarryForward(0.0);
                                                    aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);




                                                }
                                                aifUmbrella.setUpfrontAdjus(adjustUpfront);


                                            }
                                            else {
//                                        if(aif2InvestmentsBeforeMonth.getTotOfUnits() != totalNumberUnits)
                                                if (Double.compare(aif2InvestmentsBeforeMonth.getTotOfUnits(), totalNumberUnits) == 0) {
                                                    double newOpeningCorpus = aif2InvestmentsBeforeMonth.getOpeningCorpus() + aif2InvestmentsBeforeMonth.getAdditionalCorpus();
                                                    double upfrontFee=0d;
                                                    double carryForwardWithdraw=0d;


                                                    aifUmbrella.setAdditionalCorpus(0.0);
                                                    aifUmbrella.setOpeningCorpus(newOpeningCorpus);
                                                    aifUmbrella.setUpFront(0.0);
                                                    aifUmbrella.setCumulativeCorpus(newOpeningCorpus);
                                                    if(aifUmbrella.getAif2SeriesMaster().getClassType().contains("Class B1")) {
//                                                        adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 1.50 / 100));
                                                        adjustUpfront = aifUmbrella.getDistShare().doubleValue();


                                                        upfrontFee = (aifUmbrella.getCumulativeCorpus() * 1.50) / 100;
                                                        aifUmbrella.setCarryForward(aif2InvestmentsBeforeMonth.getCarryOver());
                                                        aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);




                                                    }
                                                    else {

//                                                        adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 2 / 100));
                                                        adjustUpfront = aifUmbrella.getDistShare().doubleValue();

                                                        upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                        aifUmbrella.setCarryForward(aif2InvestmentsBeforeMonth.getCarryOver());
                                                        aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);




                                                    }
                                                    aifUmbrella.setUpfrontAdjus(adjustUpfront);
                                                }
                                                else {


                                                    double newAdditionalCorpus = totalNumberUnits - aif2InvestmentsBeforeMonth.getTotOfUnits();
                                                    aifUmbrella.setAdditionalCorpus(newAdditionalCorpus);
                                                    aifUmbrella.setOpeningCorpus(0.0);
                                                    Double cumulativeCorpus= aifUmbrella.getAdditionalCorpus()+aifUmbrella.getOpeningCorpus();
                                                    aifUmbrella.setCumulativeCorpus(cumulativeCorpus);

                                                    double percentageCalc=0d;
                                                    double upfrontFee=0d;
                                                    double carryForwardWithdraw=0d;
                                                    System.out.println("class series"+aifUmbrella.getAif2SeriesMaster().getClassType());
                                                    if(aifUmbrella.getAif2SeriesMaster().getClassType().contains("Class B1")) {
                                                        percentageCalc = newAdditionalCorpus / 100 * 1.50;
//                                                        adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 1.50 / 100));
                                                        adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                        aifUmbrella.setUpFront(percentageCalc);


                                                        upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                        aifUmbrella.setCarryForward(aif2InvestmentsBeforeMonth.getCarryOver());
                                                        aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);




                                                    }
                                                    else {

                                                        percentageCalc = newAdditionalCorpus / 100 * 2;
//                                                        adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 2 / 100));
                                                        adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                        aifUmbrella.setUpFront(percentageCalc);


                                                        upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                        aifUmbrella.setCarryForward(aif2InvestmentsBeforeMonth.getCarryOver());
                                                        aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);





                                                    }
                                                    aifUmbrella.setUpfrontAdjus(adjustUpfront);


                                                }

                                            }

                                        }


                                        List<AIFUmbrella> aifUmbrellas = aifUmbrellaCalculationRepository.findByClientManagementAndFromDateAndTotOfUnits(clientManagement, sStartDate,totalNumberUnits);
                                        System.out.println("aifUmbrellas size" + aifUmbrellas.size());
                                        if (aifUmbrellas.size() == 0) {

                                            aifUmbrellaCalculationRepository.save(aifUmbrella);
                                            aif2MonthlyCalculations.add(aifUmbrella);
                                        }


//                    aif2MonthlyCalculations.add(aifUmbrella);
                                    }
//                                        else {
//                                            AIFUmbrella aifUmbrella = new AIFUmbrella();
//
//                                            aifUmbrella.setClientManagement(clientManagement);
//                                            aifUmbrella.setDistributorMaster(clientManagement.getDistributorMaster());
//                                            aifUmbrella.setRelationshipManager(clientManagement.getRelationshipManager());
//                                            aifUmbrella.setSubRM(clientManagement.getSubRM());
//                                            Float seriesUnits = aif2InvestmentsRepository.findSeriesUnits(transactionReportSeries1.getSeriesId(), product.getId());
//                                            AIF2SeriesMaster aif2SeriesMasterId = aif2SeriesMasterRepository.findById(transactionReportSeries1.getSeriesId()).get();
//                                            aifUmbrella.setAif2SeriesMaster(aif2SeriesMasterId);
//
//
//                                            aifUmbrella.setFromDate(sStartDate);
//                                            Float noOfUnits = 0f;
//                                            Double totalNumberUnits = 0d;
//                                            Float managementfee = 0f;
//                                            Float unitPrice = 0f;
//
//
//                                            AIF2ManagementFee aif2ManagementFee = aif2ManagementFeeRepository.findAIF2Management(sStart, sEnd, transactionReportSeries1.getSeriesId(), product.getId());
//
//
//                                            noOfUnits += transactionReportSeries1.getQuantity();
//                                            totalNumberUnits += transactionReportSeries1.getNetAmount();
//                                            unitPrice += transactionReportSeries1.getRate();
//                                            managementfee += (transactionReportSeries1.getQuantity() / seriesUnits) * aif2ManagementFee.getUnits();
//                                            //float managementFee = (aif2Investments.getNoOfUnits() / seriesUnits) * aif2ManagementFee.getUnits();
//
//
//                                            aifUmbrella.setNoOfUnits(noOfUnits);
//                                            aifUmbrella.setTotOfUnits(totalNumberUnits);
//                                            aifUmbrella.setManagementFee(managementfee);
//                                            aifUmbrella.setDistShare((managementfee * commissionDefinition.getDistributorComm()) / 100);
//                                            aifUmbrella.setUnitPrice(unitPrice);
//
//                                            if (commissionDefinition.getTrailUpfrontOption().equalsIgnoreCase("U")) {
//
//                                                AIFUmbrella aif2InvestmentsBeforeMonth = aifUmbrellaCalculationRepository.findByBeforeDateUmbrella(sStart, clientManagement.getId());
//
//
//                                                if (aif2InvestmentsBeforeMonth == null) {
//                                                    aifUmbrella.setAdditionalCorpus(totalNumberUnits);
//                                                    aifUmbrella.setOpeningCorpus(0.0);
//
//                                                    double percentageCalc = totalNumberUnits / 100 * 2;
//                                                    aifUmbrella.setUpFront(percentageCalc);
//
//
//                                                } else {
//                                                    if (Double.compare(aif2InvestmentsBeforeMonth.getTotOfUnits(), totalNumberUnits) == 0) {
//
//
//                                                        double newOpeningCorpus = aif2InvestmentsBeforeMonth.getOpeningCorpus() + aif2InvestmentsBeforeMonth.getAdditionalCorpus();
//                                                        aifUmbrella.setAdditionalCorpus(0.0);
//                                                        aifUmbrella.setOpeningCorpus(newOpeningCorpus);
//                                                        aifUmbrella.setUpFront(0.0);
//
//                                                    } else {
//
//
//                                                        double newAdditionalCorpus = totalNumberUnits - aif2InvestmentsBeforeMonth.getTotOfUnits();
//                                                        aifUmbrella.setAdditionalCorpus(newAdditionalCorpus);
//                                                        aifUmbrella.setOpeningCorpus(0.0);
//                                                        double percentageCalc = newAdditionalCorpus / 100 * 2;
//                                                        aifUmbrella.setUpFront(percentageCalc);
//
//                                                    }
//
//                                                }
//                                            }
//                                            List<AIFUmbrella> aifUmbrellas = aifUmbrellaCalculationRepository.findByClientManagementAndFromDate(clientManagement, sStartDate);
//                                            if (aifUmbrellas.size() == 0) {
//                                                aifUmbrellaCalculationRepository.save(aifUmbrella);
//                                                aif2MonthlyCalculations.add(aifUmbrella);
//
//                                            }
//
//
//                                        }

                                } else {
                                    System.out.println("test " + aif2MonthlyCalculationss);

                                    aif2MonthlyCalculations.addAll(aif2MonthlyCalculationss);
                                }
                            }


                        }
                    }
                    else
                    {


                        List<AIFUmbrella> aif2InvestmentsBeforeMonth = aifUmbrellaCalculationRepository.findByBeforeDateUmbrella(sStart, clientManagement.getId());
                        for(AIFUmbrella aifUmbrellass:aif2InvestmentsBeforeMonth) {
                            if (aifUmbrellass != null) {
                                AIFUmbrella aifUmbrella = new AIFUmbrella();

                                aifUmbrella.setClientManagement(aifUmbrellass.getClientManagement());
                                aifUmbrella.setDistributorMaster(aifUmbrellass.getDistributorMaster());
                                aifUmbrella.setRelationshipManager(aifUmbrellass.getRelationshipManager());
                                aifUmbrella.setSubRM(aifUmbrellass.getSubRM());
                                Float seriesUnits = aif2InvestmentsRepository.findSeriesUnits(aifUmbrellass.getAif2SeriesMaster().getId(), product.getId());

                                AIF2ManagementFee aif2ManagementFee = aif2ManagementFeeRepository.findAIF2Management(sStart, sEnd, aifUmbrellass.getAif2SeriesMaster().getId(), product.getId());
                                Float managementfee = (aifUmbrellass.getNoOfUnits() / seriesUnits) * aif2ManagementFee.getUnits();


                                aifUmbrella.setAif2SeriesMaster(aifUmbrellass.getAif2SeriesMaster());
                                aifUmbrella.setNoOfUnits(aifUmbrellass.getNoOfUnits());
                                aifUmbrella.setTotOfUnits(aifUmbrellass.getTotOfUnits());
                                aifUmbrella.setManagementFee(managementfee);
                                aifUmbrella.setDistShare((managementfee * commissionDefinition.getDistributorComm()) / 100);
                                aifUmbrella.setFromDate(sStartDate);
                                aifUmbrella.setUnitPrice(aifUmbrellass.getUnitPrice());


                                if (commissionDefinition.getTrailUpfrontOption().equalsIgnoreCase("U")) {
                                    List<AIFUmbrella> aif2InvestmentsBeforeMonth1 = aifUmbrellaCalculationRepository.findByBeforeDateUmbrella(sStart, clientManagement.getId());
                                    for(AIFUmbrella aif2InvestmentsBeforeMonthL:aif2InvestmentsBeforeMonth1) {
                                        if (aif2InvestmentsBeforeMonth1 == null) {
                                            aifUmbrella.setAdditionalCorpus(aif2InvestmentsBeforeMonthL.getTotOfUnits());
                                            aifUmbrella.setOpeningCorpus(0.0);
                                            Double cumulativeCorpus= aifUmbrella.getAdditionalCorpus()+aifUmbrella.getOpeningCorpus();
                                            aifUmbrella.setCumulativeCorpus(cumulativeCorpus);

                                            double percentageCalc = aif2InvestmentsBeforeMonthL.getTotOfUnits() / 100 * 2;
                                            aifUmbrella.setUpFront(percentageCalc);
                                            double upfrontFee=0d;
                                            double carryForwardWithdraw=0d;
                                            double adjustUpfront=0d;

                                            //
                                            if(aifUmbrella.getAif2SeriesMaster().getClassType().contains("Class B1")) {
                                                percentageCalc = aif2InvestmentsBeforeMonthL.getTotOfUnits() / 100 * 1.50;
//                                                adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 1.50 / 100));
                                                adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                aifUmbrella.setUpFront(percentageCalc);


                                                upfrontFee = (aifUmbrella.getCumulativeCorpus() * 1.50) / 100;
                                                aifUmbrella.setCarryForward(0.0);
                                                aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);





                                            }
                                            else {

                                                percentageCalc = aif2InvestmentsBeforeMonthL.getTotOfUnits() / 100 * 2;
//                                                adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 2 / 100));
                                                adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                aifUmbrella.setUpFront(percentageCalc);


                                                upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                aifUmbrella.setCarryForward(0.0);
                                                aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);




                                            }
                                            aifUmbrella.setUpfrontAdjus(adjustUpfront);


                                        }
                                        else {
//                                        if(aif2InvestmentsBeforeMonth.getTotOfUnits() != totalNumberUnits)
                                            if (Double.compare(aif2InvestmentsBeforeMonthL.getTotOfUnits(), aif2InvestmentsBeforeMonthL.getTotOfUnits()) == 0) {
                                                double newOpeningCorpus = aif2InvestmentsBeforeMonthL.getOpeningCorpus() + aif2InvestmentsBeforeMonthL.getAdditionalCorpus();

                                                double upfrontFee=0d;
                                                double carryForwardWithdraw=0d;
                                                double adjustUpfront=0d;

                                                aifUmbrella.setAdditionalCorpus(0.0);
                                                aifUmbrella.setOpeningCorpus(newOpeningCorpus);
                                                aifUmbrella.setUpFront(0.0);

                                                aifUmbrella.setCumulativeCorpus(newOpeningCorpus);
                                                if(aifUmbrella.getAif2SeriesMaster().getClassType().contains("Class B1")) {
//                                                    adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 1.50 / 100));
                                                    adjustUpfront = aifUmbrella.getDistShare().doubleValue();

                                                    upfrontFee = (aifUmbrella.getCumulativeCorpus() * 1.50) / 100;
                                                    aifUmbrella.setCarryForward(aif2InvestmentsBeforeMonthL.getCarryOver());
                                                    aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);




                                                }
                                                else {

//                                                    adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 2 / 100));
                                                    adjustUpfront = aifUmbrella.getDistShare().doubleValue();

                                                    upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                    aifUmbrella.setCarryForward(aif2InvestmentsBeforeMonthL.getCarryOver());
                                                    aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);





                                                }
                                                aifUmbrella.setUpfrontAdjus(adjustUpfront);

                                            } else {


                                                double newAdditionalCorpus = aif2InvestmentsBeforeMonthL.getTotOfUnits() - aifUmbrellass.getTotOfUnits();
                                                aifUmbrella.setAdditionalCorpus(newAdditionalCorpus);
                                                aifUmbrella.setOpeningCorpus(0.0);
                                                double percentageCalc = newAdditionalCorpus / 100 * 2;
                                                aifUmbrella.setUpFront(percentageCalc);

                                                ////////

                                                Double cumulativeCorpus= aifUmbrella.getAdditionalCorpus()+aifUmbrella.getOpeningCorpus();
                                                aifUmbrella.setCumulativeCorpus(cumulativeCorpus);

                                                double upfrontFee=0d;
                                                double carryForwardWithdraw=0d;
                                                double adjustUpfront=0d;
                                                System.out.println("class series"+aifUmbrella.getAif2SeriesMaster().getClassType());
                                                if(aifUmbrella.getAif2SeriesMaster().getClassType().contains("Class B1")) {
                                                    percentageCalc = newAdditionalCorpus / 100 * 1.50;
//                                                    adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 1.50 / 100));
                                                    adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                    aifUmbrella.setUpFront(percentageCalc);

                                                    upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                    aifUmbrella.setCarryForward(aif2InvestmentsBeforeMonthL.getCarryOver());
                                                    aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);





                                                }
                                                else {

                                                    percentageCalc = newAdditionalCorpus / 100 * 2;
//                                                    adjustUpfront = ((aifUmbrella.getCumulativeCorpus() * 2 / 100));
                                                    adjustUpfront = aifUmbrella.getDistShare().doubleValue();
                                                    aifUmbrella.setUpFront(percentageCalc);


                                                    upfrontFee = (aifUmbrella.getCumulativeCorpus() * 2) / 100;
                                                    aifUmbrella.setCarryForward(0.0);
                                                    aifUmbrella.setCarryOver(aifUmbrella.getUpFront() + aifUmbrella.getCarryForward() - adjustUpfront + carryForwardWithdraw);





                                                }
                                                aifUmbrella.setUpfrontAdjus(adjustUpfront);



                                            }

                                        }
                                    }
                                    //end
                                    aifUmbrella.setUnitPrice(aifUmbrellass.getUnitPrice());
                                    aifUmbrella.setFromDate(sStartDate);


                                }



                                List<AIFUmbrella> aifUmbrellas = aifUmbrellaCalculationRepository.findByClientManagementAndFromDateAndTotOfUnits(clientManagement, sStartDate, aifUmbrellass.getTotOfUnits());
                                System.out.println("aifUmbrellas size" + aifUmbrellas.size());
                                if (aifUmbrellas.size() == 0) {
                                    aifUmbrellaCalculationRepository.save(aifUmbrella);
                                    aif2MonthlyCalculations.add(aifUmbrella);

                                }


                            }
                        }



                    }



                } else {
                    System.out.println("no client found");
                }

            }

//            AIFDistributorFee aifDistributorFeeCurrent = aifDistributorFeeRepository.getMonthsCalculation(product.getId(), reportGeneration.getDistributorMaster1().getId(),
//                monthlyDate);
//            if (aifDistributorFeeCurrent == null) {
//                aifDistributorFee.setProduct(product);
//                aifDistributorFee.setDistributorMaster(reportGeneration.getDistributorMaster1());
//                aifDistributorFee.setStartDate(beginCalendar.getTime());
//                if (aifDistributorFeeBefore == null)
//                    aifDistributorFee.setOpeningBalFee(0f);
//                else
//                    aifDistributorFee.setOpeningBalFee(aifDistributorFeeBefore.getClosingBal());
//
//                Float distributorShare=aif2MonthlyCalculationRepository.getDistShare(reportGeneration.getDistributorMaster1().getId(),
//                    monthlyDate);
//                if(distributorShare!=null)
//                    aifDistributorFee.setDistributorShare(distributorShare);
//                else
//                    aifDistributorFee.setDistributorShare(0f);
//                Float genericPayTrailUpfront=genericPayTrailUpfrontRepository.getProductPaid(sStart,sEnd,product.getId(),"Trail",reportGeneration.getDistributorMaster1().getId());
//                Float paidAmount=(genericPayTrailUpfront==null)? 0f :genericPayTrailUpfront;
//                aifDistributorFee.setPaidAmt(paidAmount);
//                aifDistributorFee.setClosingBal(aifDistributorFee.getOpeningBalFee()+aifDistributorFee.getDistributorShare()-paidAmount);
//                aifDistributorFeeRepository.save(aifDistributorFee);
//
//            }
            beginCalendar.add(Calendar.MONTH, 1);
        }

        return aif2MonthlyCalculations;
    }


    private void aif2UmbrellaReportGeneration(List<AIFUmbrella> aifUmbrellas, DistributorMaster dm, ReportGeneration reportGeneration, HSSFSheet sheet1, String sStartTime, String sEndTime, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(dm.getDistName());
        Product product = productRepository.findByProductName("UNIFI AIF Umbrella Blend Fund - 2");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(dm.getId(), product.getId());

        if(commissionDefinition.getTrailUpfrontOption().equalsIgnoreCase("T")) {
            Double aif2Toal = 0.0;

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csRightLeftRight = workBook.createCellStyle();
            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();
            CellStyle csHorVerDate = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPercNoBorder = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");
            fFont.setBold(true);
            cs.setFont(fFont);

            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csHorVerDate.setAlignment(HorizontalAlignment.CENTER);
            csHorVerDate.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerDate.setFont(defaultFont);
            csHorVerDate.setWrapText(true);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle cellStyle = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cellStyle;

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csPlain = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);

            csPlain.setBorderLeft(BorderStyle.THIN);
            csPlain.setBorderRight(BorderStyle.THIN);

            csDFBold.setBorderLeft(BorderStyle.THIN);
            csDFBold.setBorderRight(BorderStyle.THIN);

            csPercNoBorder = csPerc;

            csPerc.setBorderLeft(BorderStyle.THIN);
            csPerc.setBorderRight(BorderStyle.THIN);

            csPercNoBorder.setBorderLeft(BorderStyle.NONE);
            csPercNoBorder.setBorderRight(BorderStyle.NONE);

            csRight.setFont(defaultFont);
            //csHorVerCenter.setFont(defaultFont);
            csDF.setFont(defaultFont);
            csPlain.setFont(defaultFont);
            //csDFBold.setFont(defaultFont);
            csPerc.setFont(defaultFont);


            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            int iRowNo = sheet1.getLastRowNum() + 1;

            HSSFRow row = sheet1.createRow(iRowNo);
            HSSFRow rowUserName = sheet1.createRow(sheet1.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(dm.getDistName().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

            HSSFRow durationFrom = sheet1.createRow(sheet1.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + "-" + sEndTime);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs
            // Format sheet
            sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheet1.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iDistCommLineNum = sheet1.getLastRowNum() + 1;
            boolean bIsCommissionSet = false;
            HSSFRow distComm = sheet1.createRow(iDistCommLineNum);
            distComm.createCell(0).setCellValue("Distributor Commission");
            distComm.getCell(0).setCellStyle(csPlainNoBorder);

            if (commissionDefinition != null) {
                distComm.createCell(1).setCellValue(commissionDefinition.getDistributorComm() / 100);
                distComm.getCell(1).setCellStyle(csPercNoBorder);
                distComm.getCell(1).setCellType(CellType.NUMERIC);
            }


            sheet1.createFreezePane(0, 8);

            iRowNo = sheet1.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheet1.createRow(iRowNo);
            // Format sheet
            headingBRSBookRow.setHeightInPoints(30);

            sheet1.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            Cell cell = row.createCell(0);
            row = sheet1.createRow(iRowNo);
            headingBRSBookRow.createCell(1).setCellValue("Client Code");
            headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(0).setCellValue("Client Name");
            headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(2).setCellValue("Series Name");
            headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(3).setCellValue("RM");
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(4).setCellValue("Sub RM");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(5).setCellValue("Subscription Amount");
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(6).setCellValue("Unit Price");
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(7).setCellValue("No. of Units");
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(8).setCellValue("Month");
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(9).setCellValue("Management Fee");
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(10).setCellValue("Amount to Distributor");
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);

            sheet1.setDefaultColumnWidth(12);
            sheet1.autoSizeColumn(0);

            String monthSeperation = "";
            System.out.println("aif umbrellas" + aifUmbrellas.size());

            for (AIFUmbrella report : aifUmbrellas) {
                System.out.println("motnh " + report.getFromDate());

                if (monthSeperation.equals("") || (!monthSeperation.equals(dateFormat.format(report.getFromDate())))) {
                    monthSeperation = dateFormat.format(report.getFromDate());
                    iRowNo = sheet1.getLastRowNum() + 1;
                    row = sheet1.createRow(iRowNo);
                    sheet1.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 10));
                    cell = row.createCell(0);
                    cell.setCellValue(monthSeperation);
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(1);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(2);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(3);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(4);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(5);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(6);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(7);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(8);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(9);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(10);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                }

                iRowNo = sheet1.getLastRowNum() + 1;
                row = sheet1.createRow(iRowNo);

                cell = row.createCell(1);
                cell.setCellValue(report.getClientManagement().getClientCode());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(0);
                cell.setCellValue(report.getClientManagement().getClientName());
                cell.setCellStyle(cellStyle);
                sheet1.autoSizeColumn(0);

                cell = row.createCell(2);
                cell.setCellValue(report.getAif2SeriesMaster().getClassType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(3);
                if (report.getClientManagement().getRelationshipManager() != null)
                    cell.setCellValue(report.getClientManagement().getRelationshipManager().getRmName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(4);
                if (report.getClientManagement().getSubRM() != null)
                    cell.setCellValue(report.getClientManagement().getSubRM().getSubName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(5);
                cell.setCellValue(report.getTotOfUnits());
                cell.setCellStyle(csDF);

                cell = row.createCell(6);
                cell.setCellValue(report.getUnitPrice());
                cell.setCellStyle(csDF);

                cell = row.createCell(7);
                cell.setCellValue(report.getNoOfUnits());
                cell.setCellStyle(csDF);

                cell = row.createCell(8);
                String reportDate = dateFormat.format(report.getFromDate());
                cell.setCellValue(reportDate);
                cell.setCellStyle(csPlanLeftRight);


                cell = row.createCell(9);
                cell.setCellValue(report.getManagementFee());
                cell.setCellStyle(csDF);

                cell = row.createCell(10);
                cell.setCellValue(report.getDistShare());
                aif2Toal += report.getDistShare();
                cell.setCellStyle(csDF);

            }
            //}
            iRowNo++;
            //}

            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);

            HSSFRow rowTotal = sheet1.createRow(sheet1.getLastRowNum() + 1);
            rowTotal.createCell(0).setCellValue("Total");
            rowTotal.getCell(0).setCellStyle(csFour);

            HSSFCell cellTotal = rowTotal.createCell(1);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(2);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(3);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(4);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(5);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(6);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(7);
            cellTotal.setCellValue("");//sPms
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(8);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(9);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(10);
            cellTotal.setCellValue((aif2Toal.floatValue()));
            cellTotal.setCellType(CellType.NUMERIC);
            cellTotal.setCellStyle(csDFBoldFour);

            cumulativeAIFSeriesBCAD.setAIFGreenName(prop.getString("aif2.total.fee"));
            cumulativeAIFSeriesBCAD.setAIFGreenValue(aif2Toal);

        }
        else {

            Double aif2Toal = 0.0;

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csRightLeftRight = workBook.createCellStyle();
            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();
            CellStyle csHorVerDate = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPercNoBorder = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");
            fFont.setBold(true);
            cs.setFont(fFont);

            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csHorVerDate.setAlignment(HorizontalAlignment.CENTER);
            csHorVerDate.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerDate.setFont(defaultFont);
            csHorVerDate.setWrapText(true);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle cellStyle = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cellStyle;

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csPlain = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);

            csPlain.setBorderLeft(BorderStyle.THIN);
            csPlain.setBorderRight(BorderStyle.THIN);

            csDFBold.setBorderLeft(BorderStyle.THIN);
            csDFBold.setBorderRight(BorderStyle.THIN);

            csPercNoBorder = csPerc;

            csPerc.setBorderLeft(BorderStyle.THIN);
            csPerc.setBorderRight(BorderStyle.THIN);

            csPercNoBorder.setBorderLeft(BorderStyle.NONE);
            csPercNoBorder.setBorderRight(BorderStyle.NONE);

            csRight.setFont(defaultFont);
            //csHorVerCenter.setFont(defaultFont);
            csDF.setFont(defaultFont);
            csPlain.setFont(defaultFont);
            //csDFBold.setFont(defaultFont);
            csPerc.setFont(defaultFont);


            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            int iRowNo = sheet1.getLastRowNum() + 1;

            HSSFRow row = sheet1.createRow(iRowNo);
            HSSFRow rowUserName = sheet1.createRow(sheet1.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(dm.getDistName().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

            HSSFRow durationFrom = sheet1.createRow(sheet1.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + "-" + sEndTime);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs
            // Format sheet
            sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheet1.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iDistCommLineNum = sheet1.getLastRowNum() + 1;
            boolean bIsCommissionSet = false;
            HSSFRow distComm = sheet1.createRow(iDistCommLineNum);
            distComm.createCell(0).setCellValue("Distributor Commission");
            distComm.getCell(0).setCellStyle(csPlainNoBorder);

            if (commissionDefinition != null) {
                distComm.createCell(1).setCellValue(commissionDefinition.getDistributorComm() / 100);
                distComm.getCell(1).setCellStyle(csPercNoBorder);
                distComm.getCell(1).setCellType(CellType.NUMERIC);
            }


            sheet1.createFreezePane(0, 8);

            iRowNo = sheet1.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheet1.createRow(iRowNo);
            // Format sheet
            headingBRSBookRow.setHeightInPoints(30);

            sheet1.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            Cell cell = row.createCell(0);
            row = sheet1.createRow(iRowNo);
            headingBRSBookRow.createCell(1).setCellValue("Client Code");
            headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(0).setCellValue("Client Name");
            headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(2).setCellValue("Series Name");
            headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(3).setCellValue("RM");
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(4).setCellValue("Sub RM");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(5).setCellValue("Subscription Amount");
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(6).setCellValue("Unit Price");
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(7).setCellValue("No. of Units");
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(8).setCellValue("Month");
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(9).setCellValue("Opening corpus");
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(10).setCellValue("Additional corpus");
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(11).setCellValue("Withdrawal");
            headingBRSBookRow.getCell(11).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(12).setCellValue("Cumulative Corpus");
            headingBRSBookRow.getCell(12).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(13).setCellValue("Carry Forward Upfront fee");
            headingBRSBookRow.getCell(13).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(14).setCellValue("Upfront Fee 2%");
            headingBRSBookRow.getCell(14).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(15).setCellValue("Management Fee");
            headingBRSBookRow.getCell(15).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(16).setCellValue("Amount to Distributor");
            headingBRSBookRow.getCell(16).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(17).setCellValue("Adjustment Against Upfront Fee");
            headingBRSBookRow.getCell(17).setCellStyle(csHorVerCenter);
            headingBRSBookRow.createCell(18).setCellValue("Carry over of Upfront Fee");
            headingBRSBookRow.getCell(18).setCellStyle(csHorVerCenter);

            sheet1.setDefaultColumnWidth(18);
            sheet1.autoSizeColumn(0);
            String monthSeperation = "";
            System.out.println("aif umbrellas" + aifUmbrellas.size());

            for (AIFUmbrella report : aifUmbrellas) {
                System.out.println("motnh++++++++++++++++++++++++++++++++++++++++++++++ " + report);

                if (monthSeperation.equals("") || (!monthSeperation.equals(dateFormat.format(report.getFromDate())))) {
                    monthSeperation = dateFormat.format(report.getFromDate());
                    iRowNo = sheet1.getLastRowNum() + 1;
                    row = sheet1.createRow(iRowNo);
                    sheet1.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 10));
                    cell = row.createCell(0);
                    cell.setCellValue(monthSeperation);
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(1);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(2);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(3);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(4);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(5);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(6);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(7);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(8);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(9);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(10);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);


                    cell = row.createCell(11);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(12);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(13);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);


                    cell = row.createCell(14);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(15);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(16);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);


                    cell = row.createCell(17);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);

                    cell = row.createCell(18);
                    cell.setCellValue("");
                    cell.setCellStyle(csHorVerCenter);


                }

                iRowNo = sheet1.getLastRowNum() + 1;
                row = sheet1.createRow(iRowNo);

                cell = row.createCell(1);
                cell.setCellValue(report.getClientManagement().getClientCode());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(0);
                cell.setCellValue(report.getClientManagement().getClientName());
                cell.setCellStyle(cellStyle);
                sheet1.autoSizeColumn(0);

                cell = row.createCell(2);
                cell.setCellValue(report.getAif2SeriesMaster().getClassType());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(3);
                if (report.getClientManagement().getRelationshipManager() != null)
                    cell.setCellValue(report.getClientManagement().getRelationshipManager().getRmName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(4);
                if (report.getClientManagement().getSubRM() != null)
                    cell.setCellValue(report.getClientManagement().getSubRM().getSubName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(5);
                cell.setCellValue(report.getTotOfUnits());
                cell.setCellStyle(csDF);

                cell = row.createCell(6);
                cell.setCellValue(report.getUnitPrice());
                cell.setCellStyle(csDF);

                cell = row.createCell(7);
                cell.setCellValue(report.getNoOfUnits());
                cell.setCellStyle(csDF);

                cell = row.createCell(8);
                String reportDate = dateFormat.format(report.getFromDate());
                cell.setCellValue(reportDate);
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(9);
                cell.setCellValue(report.getOpeningCorpus());
                cell.setCellStyle(csDF);

                cell = row.createCell(10);
                cell.setCellValue(report.getAdditionalCorpus());
                cell.setCellStyle(csDF);

                cell = row.createCell(11);
                cell.setCellValue("");
                cell.setCellStyle(csDF);


                cell = row.createCell(12);
                cell.setCellValue(report.getCumulativeCorpus());
                cell.setCellStyle(csDF);

                cell = row.createCell(13);
                cell.setCellValue(report.getCarryForward());
                cell.setCellStyle(csDF);



                cell = row.createCell(14);
                cell.setCellValue(report.getUpFront());
                cell.setCellStyle(csDF);


                cell = row.createCell(15);
                cell.setCellValue(report.getManagementFee());
                cell.setCellStyle(csDF);

                cell = row.createCell(16);
                cell.setCellValue(report.getDistShare());
                aif2Toal += report.getDistShare();
                cell.setCellStyle(csDF);

                cell = row.createCell(17);
                cell.setCellValue(report.getUpfrontAdjus());
                cell.setCellStyle(csDF);



                cell = row.createCell(18);
                cell.setCellValue(report.getCarryOver());
                cell.setCellStyle(csDF);

            }
            //}
            iRowNo++;
            //}

            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);

            HSSFRow rowTotal = sheet1.createRow(sheet1.getLastRowNum() + 1);
            rowTotal.createCell(0).setCellValue("Total");
            rowTotal.getCell(0).setCellStyle(csFour);

            HSSFCell cellTotal = rowTotal.createCell(1);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(2);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(3);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(4);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(5);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(6);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(7);
            cellTotal.setCellValue("");//sPms
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(8);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(9);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(10);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(11);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(12);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(13);
            cellTotal.setCellValue((aif2Toal.floatValue()));
            cellTotal.setCellType(CellType.NUMERIC);
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(14);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);


            cellTotal = rowTotal.createCell(15);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);


            cellTotal = rowTotal.createCell(16);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cellTotal = rowTotal.createCell(17);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);


            cellTotal = rowTotal.createCell(18);
            cellTotal.setCellValue("");
            cellTotal.setCellStyle(csDFBoldFour);

            cumulativeAIFSeriesBCAD.setAIFGreenName(prop.getString("aif2.total.fee"));
            cumulativeAIFSeriesBCAD.setAIFGreenValue(aif2Toal);


        }



    }






}

