package com.restaurant.management.service;

import com.restaurant.management.domain.*;
import com.restaurant.management.exception.product.ProductMessages;
import com.restaurant.management.exception.product.ProductNotFoundException;
import com.restaurant.management.exception.user.UserMessages;
import com.restaurant.management.exception.user.UserNotFoundException;
import com.restaurant.management.mapper.IngredientMapper;
import com.restaurant.management.repository.AccountUserRepository;
import com.restaurant.management.repository.SessionLineItemRepository;
import com.restaurant.management.repository.ProductRepository;
import com.restaurant.management.security.CurrentUser;
import com.restaurant.management.security.UserPrincipal;
import com.restaurant.management.utils.Utils;
import com.restaurant.management.web.request.product.ProductRequest;
import com.restaurant.management.web.request.product.RegisterProductRequest;
import com.restaurant.management.web.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;
    private IngredientMapper ingredientMapper;
    private SessionLineItemRepository sessionLineItemRepository;
    private AccountUserRepository accountUserRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          IngredientMapper ingredientMapper,
                          SessionLineItemRepository sessionLineItemRepository,
                          AccountUserRepository accountUserRepository) {
        this.productRepository = productRepository;
        this.ingredientMapper = ingredientMapper;
        this.sessionLineItemRepository = sessionLineItemRepository;
        this.accountUserRepository = accountUserRepository;
    }

    public Product registerProduct(RegisterProductRequest request) {

        List<Ingredient> ingredients = ingredientMapper.mapToIngredientListFromRequest(request.getIngredients());

        double price = request.getPrice();
        price = Math.floor(price * 100) / 100;

        Product newProduct = new Product.ProductBuilder()
                .setName(request.getName())
                .setCategory(request.getCategory())
                .setPrice(price)
                .setIngredients(ingredients)
                .build();

        productRepository.save(newProduct);

        return newProduct;
    }

    public Product registerProduct(@CurrentUser UserPrincipal currentUser, RegisterProductRequest request) {

        AccountUser accountUser = accountUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(UserMessages.ID_NOT_FOUND.getMessage()));

        String uniqueProductId = Utils.generateProductUniqueId(7);

        List<Ingredient> ingredients = ingredientMapper.mapToIngredientListFromRequest(request.getIngredients());

        double price = request.getPrice();
        price = Math.floor(price * 100) / 100;

        Product newProduct = new Product.ProductBuilder()
                .setUniqueId(uniqueProductId)
                .setName(request.getName())
                .setCategory(request.getCategory())
                .setPrice(price)
                .setIngredients(ingredients)
                .build();

        newProduct.setRestaurantInfo(accountUser.getRestaurantInfo());

        productRepository.save(newProduct);

        return newProduct;
    }

    public Product updateProduct(ProductRequest productRequest, @CurrentUser UserPrincipal currentUser) {
        AccountUser accountUser = accountUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(UserMessages.ID_NOT_FOUND.getMessage()));

        Long restaurantId = accountUser.getRestaurantInfo().getId();

        Product product = productRepository.findByIdAndRestaurantInfoId(productRequest.getId(), restaurantId)
                .orElseThrow(() -> new ProductNotFoundException(
                        ProductMessages.PRODUCT_ID_NOT_FOUND.getMessage() + productRequest.getId()
                ));

        List<Ingredient> ingredients = ingredientMapper.mapToIngredientListFromRequest(productRequest.getIngredients());

        Stream.of(product).forEach(p -> {
            p.setName(productRequest.getName());
            p.setPrice(productRequest.getPrice());
            p.setCategory(productRequest.getCategory());
            p.setIngredients(ingredients);
        });

        productRepository.save(product);

        return product;
    }

    public Product getProductById(Long id, @CurrentUser UserPrincipal currentUser) {
        AccountUser accountUser = accountUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(UserMessages.ID_NOT_FOUND.getMessage()));

        Long restaurantId = accountUser.getRestaurantInfo().getId();

        Product product = productRepository.findByIdAndRestaurantInfoId(id, restaurantId)
                .orElseThrow(() -> new ProductNotFoundException(ProductMessages.PRODUCT_ID_NOT_FOUND.getMessage() + id));

        return product;
    }

    public Page<Product> getAllByRestaurant(Pageable pageable, @CurrentUser UserPrincipal currentUser) {
        AccountUser accountUser = accountUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(UserMessages.ID_NOT_FOUND.getMessage()));

        return productRepository.findByRestaurantInfo(pageable, accountUser.getRestaurantInfo());
    }

    public ApiResponse deleteById(Long id, @CurrentUser UserPrincipal currentUser) {
        AccountUser accountUser = accountUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(UserMessages.ID_NOT_FOUND.getMessage()));

        Long restaurantId = accountUser.getRestaurantInfo().getId();

        Optional<Product> product = productRepository.findByIdAndRestaurantInfoId(id, restaurantId);

        if (product.isPresent()) {
            List<SessionLineItem> sessionLineItems = sessionLineItemRepository.findAllByProductId(id);

            sessionLineItemRepository.deleteAll(sessionLineItems);

            productRepository.deleteProductById(id);

            return new ApiResponse(true, ProductMessages.PRODUCT_DELETED.getMessage());
        } else {
            throw new ProductNotFoundException(ProductMessages.PRODUCT_ID_NOT_FOUND.getMessage() + id);
        }
    }

    //ADMIN PRODUCT SERVICE

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) {
            throw new ProductNotFoundException(ProductMessages.PRODUCT_ID_NOT_FOUND.getMessage());
        }
        return product.get();
    }

    public Product updateProduct(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new ProductNotFoundException(
                        ProductMessages.PRODUCT_ID_NOT_FOUND.getMessage() + productRequest.getId()
                ));

        List<Ingredient> ingredients = ingredientMapper.mapToIngredientListFromRequest(productRequest.getIngredients());

        Stream.of(product).forEach(p -> {
            p.setName(productRequest.getName());
            p.setPrice(productRequest.getPrice());
            p.setCategory(productRequest.getCategory());
            p.setIngredients(ingredients);
        });

        productRepository.save(product);

        return product;
    }
}