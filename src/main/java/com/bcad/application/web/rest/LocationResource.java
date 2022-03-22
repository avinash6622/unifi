package com.bcad.application.web.rest;

import com.bcad.application.config.Constants;
import com.bcad.application.domain.Location;
import com.bcad.application.repository.LocationRepository;
import com.bcad.application.security.AuthoritiesConstants;
import com.bcad.application.service.dto.UserDTO;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private final LocationRepository locationRepository;

    public LocationResource(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @PostMapping("/location")
    @Timed
    public ResponseEntity<Location> createLocation(@RequestBody Location location) throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);

        Location result = locationRepository.save(location);

        return ResponseEntity.created(new URI("/api/location/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Location is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }
    @GetMapping("/location/{id}")
    @Timed
    public Optional<Location> getLocation(@PathVariable Long id){
        Optional<Location> result = locationRepository.findById(id);
        return result;

    }
    @PutMapping("/location")
    @Timed
    public ResponseEntity<Location> updateLocation(@RequestBody Location location)
        throws URISyntaxException {

        Location result = locationRepository.save(location);

        return ResponseEntity.created(new URI("/api/location/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Location is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/location/{id}")
    @Timed
    public ResponseEntity<Location> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location: {}", id);
        Optional<Location> result= locationRepository.findById(id);
       locationRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + id,id.toString())).build();
    }
    @GetMapping("/locations")
    @Timed
    public ResponseEntity<List<Location>> getAllLocation(Pageable pageable) {
        final Page<Location> page = locationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/location");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
