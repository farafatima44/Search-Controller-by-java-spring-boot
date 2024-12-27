package com.mockcompany.webapp.controller;

import com.mockcompany.webapp.api.SearchReportResponse;
import com.mockcompany.webapp.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 * This controller generates a report that counts the total number of products and
 * the number of products that match a set of predefined important search terms.
 */
@RestController
public class ProductReportController {

    // List of predefined important terms for the report
    private static final String[] KEY_TERMS = new String[] {
            "Cool", "Amazing", "Perfect", "Kids"
    };

    private final EntityManager entityManager;
    private final ProductSearchService productSearchService;

    /**
     * Constructor for injecting the required services using @Autowired
     * @param entityManager - The EntityManager for querying the database
     * @param productSearchService - The service responsible for searching products
     */
    @Autowired
    public ProductReportController(EntityManager entityManager, ProductSearchService productSearchService) {
        this.entityManager = entityManager;
        this.productSearchService = productSearchService;
    }

    /**
     * This method generates a report by querying the total number of products in the database
     * and counting the number of products that match each key term from the predefined list.
     * 
     * @return A report response containing the product count and the search term hits
     */
    @GetMapping("/api/products/report")
    public SearchReportResponse generateReport() {
        // Retrieve the total count of products from the database using a simple query
        Number totalProductCount = (Number) this.entityManager.createQuery("SELECT count(item) FROM ProductItem item")
                                                                .getSingleResult();

        // Initialize a map to store the term-based search counts
        Map<String, Integer> termHits = new HashMap<>();

        // For each key term, perform a search and count the results
        for (String term : KEY_TERMS) {
            // Search for the term and count how many products match
            termHits.put(term, productSearchService.searchProducts(term).size());
        }

        // Create and populate the response object
        SearchReportResponse reportResponse = new SearchReportResponse();
        reportResponse.setProductCount(totalProductCount.intValue());
        reportResponse.setSearchTermHits(termHits);

        // Return the generated report response
        return reportResponse;
    }
}
