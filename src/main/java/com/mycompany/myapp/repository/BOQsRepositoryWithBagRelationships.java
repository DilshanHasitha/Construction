package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BOQs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BOQsRepositoryWithBagRelationships {
    Optional<BOQs> fetchBagRelationships(Optional<BOQs> bOQs);

    List<BOQs> fetchBagRelationships(List<BOQs> bOQs);

    Page<BOQs> fetchBagRelationships(Page<BOQs> bOQs);
}
