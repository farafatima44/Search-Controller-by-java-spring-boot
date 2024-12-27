package com.mockcompany.webapp.controller;

/*
 * Import necessary dependencies to use Spring, utility classes, and repository.
 */
import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/* Import utility classes to handle collections */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The controller class for handling product searches via the /api/products/search endpoint.
 * This class is annotated with @RestController, which marks it as a Spring RESTful API controller.
 */
@RestController
public class ProductSearchController {

    // Declare the repository to interact with the product database
    private final ProductItemRepository productItemRepository;

    /**
     * Constructor for injecting the repository instance using @Autowired
     * @param productItemRepository - The product item repository to perform database operations
     */
    @Autowired
    public ProductSearchController(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    /**
     * This method handles GET requests to /api/products/search with a query parameter.
     * It searches for products whose name or description matches the given query.
     * @param query - The search term provided by the user
     * @return A collection of ProductItem objects that match the search criteria
     */
    @GetMapping("/api/products/search")
    public Collection<ProductItem> searchProducts(@RequestParam("query") String query) {
        // Fetch all product items from the repository
        Iterable<ProductItem> allItems = productItemRepository.findAll();
        List<ProductItem> matchingItems = new ArrayList<>();

        // Determine if an exact match is requested (using quotes in the query)
        boolean isExactMatch = query.startsWith("\"") && query.endsWith("\"");

        // If exact match, remove the quotes and check for precise matches
        if (isExactMatch) {
            query = query.substring(1, query.length() - 1);
        } else {
            // If not an exact match, convert the query to lowercase for case-insensitive searching
            query = query.toLowerCase();
        }

        // Iterate through all product items to find matches
        for (ProductItem item : allItems) {
            boolean nameMatch;
            boolean descriptionMatch;

            // Check if we're doing an exact match or a partial (contains) match
            if (isExactMatch) {
                nameMatch = query.equals(item.getName());
                descriptionMatch = query.equals(item.getDescription());
            } else {
                nameMatch = item.getName().toLowerCase().contains(query);
                descriptionMatch = item.getDescription().toLowerCase().contains(query);
            }

            // If either name or description matches, add the item to the result list
            if (nameMatch || descriptionMatch) {
                matchingItems.add(item);
            }
        }

        // Return the list of matching items
        return matchingItems;
    }
}
