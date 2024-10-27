package org.zelator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zelator.entity.Intention;

public interface IntentionRepository extends JpaRepository<Intention, Long> {


}
