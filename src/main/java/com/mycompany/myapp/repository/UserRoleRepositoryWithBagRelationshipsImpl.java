package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserRole;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UserRoleRepositoryWithBagRelationshipsImpl implements UserRoleRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserRole> fetchBagRelationships(Optional<UserRole> userRole) {
        return userRole.map(this::fetchUserPermissions);
    }

    @Override
    public Page<UserRole> fetchBagRelationships(Page<UserRole> userRoles) {
        return new PageImpl<>(fetchBagRelationships(userRoles.getContent()), userRoles.getPageable(), userRoles.getTotalElements());
    }

    @Override
    public List<UserRole> fetchBagRelationships(List<UserRole> userRoles) {
        return Optional.of(userRoles).map(this::fetchUserPermissions).orElse(Collections.emptyList());
    }

    UserRole fetchUserPermissions(UserRole result) {
        return entityManager
            .createQuery(
                "select userRole from UserRole userRole left join fetch userRole.userPermissions where userRole is :userRole",
                UserRole.class
            )
            .setParameter("userRole", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<UserRole> fetchUserPermissions(List<UserRole> userRoles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userRoles.size()).forEach(index -> order.put(userRoles.get(index).getId(), index));
        List<UserRole> result = entityManager
            .createQuery(
                "select distinct userRole from UserRole userRole left join fetch userRole.userPermissions where userRole in :userRoles",
                UserRole.class
            )
            .setParameter("userRoles", userRoles)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
