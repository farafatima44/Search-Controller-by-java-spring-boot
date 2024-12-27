package com.mockcompany.webapp.service;

import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This service class provides the core logic for searching products based on a query string.
 * The query can be searched against the product name and description.
 * It supports case-insensitive search and exact matches when the query is wrapped in quotes.
 */
@Service
public class ProductSearchService {

    private final ProductItemRepository productItemRepository;

    /**
     * Constructor for injecting the ProductItemRepository instance using @Autowired
     * @param productItemRepository - The repository for accessing the product items
     */
    @Autowired
    public ProductSearchService(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    /**
     * Performs a search operation on product items, matching either the name or description based on the query.
     * It supports exact matches (when query is wrapped in quotes) and case-insensitive matches.
     * 
     * @param query - The search string input by the user
     * @return A collection of ProductItem objects that match the query
     */
    public Collection<ProductItem> searchProducts(String query) {
        // Fetch all product items from the repository
        Iterable<ProductItem> allItems = productItemRepository.findAll();
        List<ProductItem> matchingProducts = new ArrayList<>();

        // Determine if the query is for exact matches based on quotes
        boolean isExactMatch = query.startsWith("\"") && query.endsWith("\"");

        // If exact match, remove the quotes
        if (isExactMatch) {
            query = query.substring(1, query.length() - 1);
        } else {
            // Convert query to lowercase for case-insensitive search
            query = query.toLowerCase();
        }

        // Iterate over all products and filter based on the search criteria
        for (ProductItem item : allItems) {
            boolean nameMatch;
            boolean descriptionMatch;

            // Check exact match or partial match for both name and description
            if (isExactMatch) {
                nameMatch = query.equals(item.getName());
                descriptionMatch = query.equals(item.getDescription());
            } else {
                nameMatch = item.getName().toLowerCase().contains(query);
                descriptionMatch = item.getDescription().toLowerCase().contains(query);
            }

            // If either name or description matches, add the product to the results list
            if (nameMatch || descriptionMatch) {
                matchingProducts.add(item);
            }
        }

        // Return the filtered list of products
        return matchingProducts;
    }
}
