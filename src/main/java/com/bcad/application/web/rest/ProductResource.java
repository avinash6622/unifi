package com.bcad.application.web.rest;

import com.bcad.application.domain.Product;
import com.bcad.application.repository.ProductRepository;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductRepository productRepository;

    public ProductResource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/product")
    @Timed
    public ResponseEntity<Product> createProduct(@RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to save Product : {}", product);

        Product result = productRepository.save(product);

        return ResponseEntity.created(new URI("/api/product/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Product is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }
    @GetMapping("/product/{id}")
    @Timed
    public Optional<Product> getproduct(@PathVariable Long id){
        Optional<Product> result = productRepository.findById(id);
        return result;

    }
    @PutMapping("/product")
    @Timed
    public ResponseEntity<Product> updateproduct(@RequestBody Product product)
        throws URISyntaxException {

        Product result = productRepository.save(product);

        return ResponseEntity.created(new URI("/api/product/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Product is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/product/{id}")
    @Timed
    public ResponseEntity<Product> deleteproduct(@PathVariable Long id) {
        log.debug("REST request to delete Product: {}", id);
        Optional<Product> result= productRepository.findById(id);
       productRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A Product is deleted with identifier " + id,id.toString())).build();
    }

    @GetMapping("/products")
    @Timed
    public ResponseEntity<List<Product>> getAllProduct(Pageable pageable) {
        final Page<Product> page = productRepository.findAll(pageable);
         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product");
     return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/aif2products")
    @Timed
    public List<Product> getAllProduct() {
        List<String > products = new ArrayList<String>();
        products.add("AIF2");
        products.add("AIF Blend");
        products.add("UNIFI AIF Umbrella Blend Fund - 2");
        return productRepository.getAIF2andAIF_Blend(products);
    }

    @GetMapping("/produc")
    @Timed
    public List<Product> getProduct() {
        return productRepository.findAll();
    }

}
