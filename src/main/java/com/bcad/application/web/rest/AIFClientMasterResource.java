package com.bcad.application.web.rest;

import com.bcad.application.domain.*;
import com.bcad.application.repository.AIFClientMasterRepository;
import com.bcad.application.security.SecurityUtils;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AIFClientMasterResource {

    private final Logger log = LoggerFactory.getLogger(AIFClientMasterResource.class);

    private final AIFClientMasterRepository aifClientMasterRepository;
    private final UserResource userResource;

    public AIFClientMasterResource(AIFClientMasterRepository aifClientMasterRepository, UserResource userResource) {
        this.aifClientMasterRepository = aifClientMasterRepository;
        this.userResource = userResource;
    }

    @PersistenceContext
    EntityManager entityManager;

    @PostMapping("/aif-client")
    @Timed
    public ResponseEntity<AIFClientMaster> createAIFClient(@RequestBody AIFClientMaster aifClientMaster) throws URISyntaxException {
        log.debug("REST request to save DistributorOption : {}", aifClientMaster);

        AIFClientMaster result = aifClientMasterRepository.save(aifClientMaster);

        return ResponseEntity.created(new URI("/api/aif-client/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A AIFClient is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }


    @GetMapping("/aif-client/{id}")
    @Timed
    public Optional<AIFClientMaster> getAIFClient(@PathVariable Long id) {
        Optional<AIFClientMaster> result = aifClientMasterRepository.findById(id);
        return result;

    }

    @PutMapping("/aif-client")
    @Timed
    public ResponseEntity<AIFClientMaster> updateAIFClient(@RequestBody AIFClientMaster aifClientMaster)
        throws URISyntaxException {

        AIFClientMaster result = aifClientMasterRepository.save(aifClientMaster);

        return ResponseEntity.created(new URI("/api/aif-client/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A AIFClient is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/aif-client/{id}")
    @Timed
    public ResponseEntity<AIFClientMaster> deleteAIFClient(@PathVariable Long id) {
        log.debug("REST request to delete AIFClientMaster: {}", id);
        aifClientMasterRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A AIFClient is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/aif-clients")
    @Timed
    public ResponseEntity<List<AIFClientMaster>> getAllAIF(Pageable pageable) {
        Optional<User> user = userResource.getCurrentUse(SecurityUtils.getCurrentUserLogin().get());
        Page<AIFClientMaster> page = null;
        if (user.get().getDistributorMaster() != null) {
            page = aifClientMasterRepository.findByDistributorMaster(user.get().getDistributorMaster(), pageable);
        } else if (user.get().getRelationshipManager() != null) {
            page = aifClientMasterRepository.findByRelationshipManager(user.get().getRelationshipManager(), pageable);
        } else {
            page = aifClientMasterRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/aif-clients/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/AIF-search")
    @Timed
    public List<AIFClientMaster> postAIF(@RequestBody AIFClientMaster aifClientMaster) {
        List<AIFClientMaster> result = aifClientMasterRepository.findClientCode(aifClientMaster.getClientCode());
        return result;
    }

    @GetMapping("/aif-clients-all")
    @Timed
    public List<AIFClientMaster> getAllAIFClients() {
        List<AIFClientMaster>  aifClientMasters= aifClientMasterRepository.findAll();
        System.out.println(aifClientMasters.size());
        return aifClientMasters;
    }

    @PostMapping("/AIF-search-all")
    @Timed
    public List<AIFClientMaster> searchClient(@RequestBody ClientManagementSearch clientManagementSearch) {
        String sql="";
        if(clientManagementSearch.getClientCodes()!=null && clientManagementSearch.getClientCodes().size()!=0)
            sql="client_code in("+ StringUtils.join(clientManagementSearch.getClientCodes(),',')+")";
        if(clientManagementSearch.getClientNames()!=null && clientManagementSearch.getClientNames().size()!=0)
            sql=(!sql.equals("")?sql.concat(" and client_name in("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'")).collect(Collectors.toList()))+")"):"client_name in" +
                "("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()))+")");
        if(clientManagementSearch.getDistMasterId()!=null && clientManagementSearch.getDistMasterId().size()!=0)
            sql=(!sql.equals("")? sql.concat(" and dist_id in("+StringUtils.join(clientManagementSearch.getDistMasterId(),',')+")") :
                " dist_id in("+StringUtils.join(clientManagementSearch.getDistMasterId(),',')+")");

        if(clientManagementSearch.getRmId()!=null && clientManagementSearch.getRmId().size()!=0)
            sql=(!sql.equals("")? sql.concat(" and rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")"):
                " rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")");

        Query query=entityManager.createNativeQuery("select * from aif_client_master where "+sql,AIFClientMaster.class);

        List<AIFClientMaster> result = query.getResultList();
        System.out.println(result);
        return result;

    }
}
