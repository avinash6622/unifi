package com.bcad.application.web.rest;

import com.bcad.application.domain.Authority;
import com.bcad.application.domain.Location;
import com.bcad.application.repository.AuthorityRepository;
import com.bcad.application.repository.LocationRepository;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private final AuthorityRepository authorityRepository;

    public AuthorityResource(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @PostMapping("/authority")
    @Timed
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) throws URISyntaxException {
        log.debug("REST request to save User : {}", authority);

        Authority result = authorityRepository.save(authority);

        return ResponseEntity.created(new URI("/api/authority/" + result.getName()))
            .headers(HeaderUtil.createAlert( "A Authority is created with identifier " + result.getName(), result.getName()))
            .body(result);

    }
}
