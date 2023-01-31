package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BOQs;
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
public class BOQsRepositoryWithBagRelationshipsImpl implements BOQsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BOQs> fetchBagRelationships(Optional<BOQs> bOQs) {
        return bOQs.map(this::fetchBoqDetails);
    }

    @Override
    public Page<BOQs> fetchBagRelationships(Page<BOQs> bOQs) {
        return new PageImpl<>(fetchBagRelationships(bOQs.getContent()), bOQs.getPageable(), bOQs.getTotalElements());
    }

    @Override
    public List<BOQs> fetchBagRelationships(List<BOQs> bOQs) {
        return Optional.of(bOQs).map(this::fetchBoqDetails).orElse(Collections.emptyList());
    }

    BOQs fetchBoqDetails(BOQs result) {
        return entityManager
            .createQuery("select bOQs from BOQs bOQs left join fetch bOQs.boqDetails where bOQs is :bOQs", BOQs.class)
            .setParameter("bOQs", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<BOQs> fetchBoqDetails(List<BOQs> bOQs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, bOQs.size()).forEach(index -> order.put(bOQs.get(index).getId(), index));
        List<BOQs> result = entityManager
            .createQuery("select distinct bOQs from BOQs bOQs left join fetch bOQs.boqDetails where bOQs in :bOQs", BOQs.class)
            .setParameter("bOQs", bOQs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
