package com.bcad.application.web.rest;

import com.bcad.application.bean.WSBean;
import com.bcad.application.bean.WSReport;
import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.PMSClientMaster;
import com.bcad.application.domain.ReportGeneration;
import com.bcad.application.repository.PMSClientMasterRepository;
import com.bcad.application.service.Calculation.WSSpectrumService;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WealthSpectrumResource {

    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final WSSpectrumService wsSpectrumService;

    public WealthSpectrumResource(PMSClientMasterRepository pmsClientMasterRepository,WSSpectrumService wsSpectrumService) {
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.wsSpectrumService = wsSpectrumService;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");

    @PostMapping("/report-ws")
    @Timed
    public String wsReportGeneration(@RequestBody WSReport wsReport) throws URISyntaxException, Exception {
        List<WSBean> wsBeanList = new ArrayList<>();

        List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findAll();
        wsBeanList = wsSpectrumService.generateWealthSpeactrum(pmsClientMasters,wsReport.getStartDate(),wsReport.getEndDate(),wsReport.getComments());

        wsSpectrumService.generateClientReport(wsBeanList);
        return null;
    }
    }
