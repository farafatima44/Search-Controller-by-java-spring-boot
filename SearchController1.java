package com.mockcompany.webapp.controller;

import com.mockcompany.webapp.model.ProductItem;
import com.mockcompany.webapp.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * This controller is responsible for handling search requests for products.
 * It exposes an endpoint to search products by name or description based on the provided query.
 */
@RestController
public class ProductSearchController {

    // Instance of the service responsible for searching products
    private final ProductSearchService productSearchService;

    /**
     * Constructor for injecting the ProductSearchService dependency.
     * @param productSearchService The service used to perform product searches.
     */
    @Autowired
    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    /**
     * This method handles GET requests to search for products based on a query string.
     * The query string is expected to be provided as a request parameter named "query".
     *
     * @param query The search query to filter products by name or description.
     * @return A collection of ProductItem objects that match the query.
     */
    @GetMapping("/api/products/search")
    public Collection<ProductItem> searchProducts(@RequestParam("query") String query) {
        // Delegate the search logic to the service class for better separation of concerns
        return productSearchService.search(query);
    }
}
