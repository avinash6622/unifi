package com.bcad.application.web.rest;

import com.bcad.application.domain.AIFClientMaster;
import com.bcad.application.domain.BCADPMSNav;
import com.bcad.application.domain.PMSNav;
import com.bcad.application.domain.ClientManagement;
import com.bcad.application.domain.DistributorOption;
import com.bcad.application.repository.AIFClientMasterRepository;
import com.bcad.application.repository.BCADPMSNavRepository;
import com.bcad.application.service.InvestmentDateFile;
import com.bcad.application.service.PMSUploadService;
import com.bcad.application.repository.ClientManagementRepository;
import com.bcad.application.repository.DistributorOptionRepository;
import com.bcad.application.web.rest.util.HeaderUtil;
import org.springframework.http.HttpStatus;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BCADPMSNavResource {

    private final Logger log = LoggerFactory.getLogger(BCADPMSNavResource.class);

    private final ClientManagementRepository clientManagementRepository;
    private final BCADPMSNavRepository  bcadpmsNavRepository;
    private final DistributorOptionRepository distributorOptionRepository;
    private final PMSUploadService pmsUploadService;
    private final InvestmentDateFile investmentDateFile;

    public BCADPMSNavResource(ClientManagementRepository clientManagementRepository, BCADPMSNavRepository bcadpmsNavRepository,
                              DistributorOptionRepository distributorOptionRepository,
                              PMSUploadService pmsUploadService,InvestmentDateFile investmentDateFile) {
        this.clientManagementRepository = clientManagementRepository;
        this.bcadpmsNavRepository = bcadpmsNavRepository;
        this.distributorOptionRepository = distributorOptionRepository;
        this.pmsUploadService = pmsUploadService;
        this.investmentDateFile = investmentDateFile;
    }

    @PostMapping("/pms-nav-client")
    @Timed
    public ResponseEntity<ClientManagement> createClientManagement(@RequestBody DistributorOption distributorOption) throws URISyntaxException {
        log.debug("REST request to save DistributorOption : {}", distributorOption);

        List<BCADPMSNav> result = bcadpmsNavRepository.findByPercentageComm(2f);
        Optional<DistributorOption> distributorOption1 = distributorOptionRepository.findById(distributorOption.getId());

        for(BCADPMSNav bcad:result)
        {
            Optional<ClientManagement> clientManagement=clientManagementRepository.findById(bcad.getClientManagement().getId());
            clientManagement.get().setDistributorOption(distributorOption1.get());
            clientManagementRepository.save(clientManagement.get());
        }
        return null;

    }

    @GetMapping("/pmsNavClientsByID")
    public ResponseEntity<String> getPMSNavClients() {
        int result = investmentDateFile.getBCADClients();

        return new ResponseEntity<String>("{\"TotalUpdatedRecords\":" + result + "}", null, HttpStatus.OK);
    }

    @GetMapping("/pmsnavclientsBetweenDates")
   public ResponseEntity<String> getAllPMSNavClientsBetweenDates(){

        List<BCADPMSNav> result = bcadpmsNavRepository.getAllClientsBetweenDates();
        BCADPMSNav bcadpmsNav=new BCADPMSNav();

       for(BCADPMSNav bpn:result){

           ClientManagement clients = clientManagementRepository.findByClientCode(bpn.getCodeScheme());

           bcadpmsNav.setClientManagement(clients);
           bcadpmsNavRepository.save(bcadpmsNav);
       }

       return new ResponseEntity<String>("{\"TotalUpdatedRecords\":" + result + "}", null, HttpStatus.OK);
   }

}
