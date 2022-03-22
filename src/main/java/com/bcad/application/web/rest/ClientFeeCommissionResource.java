package com.bcad.application.web.rest;

import com.bcad.application.domain.ClientFeeCommission;
import com.bcad.application.domain.ClientManagementSearch;
import com.bcad.application.repository.ClientFeeCommissionRepository;
import com.bcad.application.repository.PMSClientMasterRepository;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientFeeCommissionResource {

    private final Logger log = LoggerFactory.getLogger(AIFClientMasterResource.class);

    private final ClientFeeCommissionRepository clientFeeCommissionRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;

    public ClientFeeCommissionResource(ClientFeeCommissionRepository clientFeeCommissionRepository,
                                       PMSClientMasterRepository pmsClientMasterRepository) {
        this.clientFeeCommissionRepository = clientFeeCommissionRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
    }

    @PersistenceContext
    EntityManager entityManager;

    @PostMapping("/clientFee")
    @Timed
    public ResponseEntity<ClientFeeCommission> createClientFeeCommission(@RequestBody ClientFeeCommission clientFeeCommission) throws URISyntaxException {
        log.debug("REST request to save ClientFeeCommission : {}", clientFeeCommission);

        ClientFeeCommission result = clientFeeCommissionRepository.save(clientFeeCommission);

        return ResponseEntity.created(new URI("/api/clientFee/" + result.getPmsClientMaster().getClientName()))
            .headers(HeaderUtil.createAlert("A ClientFeeCommission is created with identifier " + result.getPmsClientMaster().getClientName(), result.getPmsClientMaster().getClientName()))
            .body(result);

    }


    @GetMapping("/client-fee/{id}")
    @Timed
    public Optional<ClientFeeCommission> getClientFeeCommission(@PathVariable Long id) {
        Optional<ClientFeeCommission> result = clientFeeCommissionRepository.findById(id);
        return result;

    }

    @PutMapping("/client-fee")
    @Timed
    public ResponseEntity<ClientFeeCommission> updateClientFeeCommission(@RequestBody ClientFeeCommission clientFeeCommission)
        throws URISyntaxException {

        ClientFeeCommission result = clientFeeCommissionRepository.save(clientFeeCommission);

        return ResponseEntity.created(new URI("/api/client-fee/" + result.getId().toString())).headers(HeaderUtil
            .createAlert("A user is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }


    @DeleteMapping("/client-fee/{id}")
    @Timed
    public ResponseEntity<ClientFeeCommission> deleteClientFeeCommission(@PathVariable Long id) {
        log.debug("REST request to delete AIFClientMaster: {}", id);
        clientFeeCommissionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A AIFClient is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/client-fees")
    @Timed
    public ResponseEntity<List<ClientFeeCommission>> getAllClientFeeCommission(Pageable pageable) {
        final Page<ClientFeeCommission> page = clientFeeCommissionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api//client-fees/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/clientFee-search-all")
    @Timed
    public List<ClientFeeCommission> searchClient(@RequestBody ClientManagementSearch clientManagementSearch) {
        String sql = "";
        List<String> clientIds = new ArrayList<>();
        if (clientManagementSearch.getDistMasterId().size() != 0) {
            List<Integer> nCheck = pmsClientMasterRepository.findDistributorPMSId(clientManagementSearch.getDistMasterId());
          /*  Query query = entityManager.createNativeQuery("select distinct(id) from pms_client_master where " +
                "dist_id in (" + StringUtils.join(clientManagementSearch.getDistMasterId(), ',') + ")");
            List<Integer> nCheck=query.getResultList();*/
            for (int i = 0; i < nCheck.size(); ++i) {
                clientIds.add(nCheck.get(i).toString());
            }
            //clientIds.clear();
            //   clientIds= Lists.transform(nCheck, number -> (long)(number));
            System.out.println(clientIds);
        }


        if ((clientManagementSearch.getClientCodes().size() != 0) ||
            (clientIds.size() != 0)) {
            if ((clientIds.size() != 0 && clientManagementSearch.getClientCodes().size() == 0) ||
                clientIds.size() == 0 && clientManagementSearch.getClientCodes().size() != 0) {
                clientManagementSearch.getClientCodes().addAll(clientIds);
            } else {
                List<String> intersect = clientManagementSearch.getClientCodes().stream()
                    .filter(clientIds::contains)
                    .collect(Collectors.toList());
                clientManagementSearch.getClientCodes().clear();
                clientManagementSearch.getClientCodes().addAll(intersect);
            }
            sql = "pms_client_id in(" + StringUtils.join(clientManagementSearch.getClientCodes(), ',') + ")";
        }

        Query query = entityManager.createNativeQuery("select * from client_fee_commision where " + sql, ClientFeeCommission.class);

        List<ClientFeeCommission> result = query.getResultList();
        //System.out.println(result);
        return result;

    }

    @GetMapping("/clientfee-commission-all")
    @Timed
    public List<ClientFeeCommission> getAllClientFeeCommission() {
        List<ClientFeeCommission>  clientFeeCommission= clientFeeCommissionRepository.findAll();
        return clientFeeCommission;
    }
}
