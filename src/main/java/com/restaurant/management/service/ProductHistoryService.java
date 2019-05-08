package com.restaurant.management.service;

import com.restaurant.management.audit.query.AuditQueryResult;
import com.restaurant.management.audit.query.AuditQueryUtils;
import com.restaurant.management.domain.AccountUser;
import com.restaurant.management.domain.Product;
import com.restaurant.management.domain.history.ProductHistory;
import com.restaurant.management.exception.user.UserMessages;
import com.restaurant.management.exception.user.UserNotFoundException;
import com.restaurant.management.repository.AccountUserRepository;
import com.restaurant.management.repository.history.ProductHistoryRepository;
import com.restaurant.management.security.CurrentUser;
import com.restaurant.management.security.UserPrincipal;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductHistoryService implements ProductHistoryRepository {

    private AccountUserRepository accountUserRepository;

    private static final String RESTAURANT_INFO_ID = "restaurantInfo";

    @Autowired
    public ProductHistoryService(AccountUserRepository accountUserRepository) {
        this.accountUserRepository = accountUserRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<ProductHistory> productRevisions(Long productId,
                                                 @CurrentUser UserPrincipal currentUser) {

        AccountUser accountUser = accountUserRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(UserMessages.ID_NOT_FOUND.getMessage()));

        Long restaurantId = accountUser.getRestaurantInfo().getId();

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery auditQuery = auditReader.createQuery()
                .forRevisionsOfEntity(Product.class, false, true)
                .add(AuditEntity.id().eq(productId))
                .add(AuditEntity.relatedId(RESTAURANT_INFO_ID).eq(restaurantId));

        return AuditQueryUtils.getAuditQueryResult(auditQuery, Product.class).stream()
                .map(ProductHistoryService::getProductHistory)
                .collect(Collectors.toList());
    }

    private static ProductHistory getProductHistory(AuditQueryResult<Product> auditQueryResult) {
        return new ProductHistory(
                auditQueryResult.getEntity(),
                auditQueryResult.getRevision().getRevisionNumber(),
                auditQueryResult.getRevisionType(),
                auditQueryResult.getEntity().getCreatedAt(),
                auditQueryResult.getEntity().getUpdatedAt(),
                auditQueryResult.getEntity().getCreatedBy(),
                auditQueryResult.getEntity().getUpdatedBy()
        );
    }
}
