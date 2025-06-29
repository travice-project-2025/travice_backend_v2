package com.gmg.travice.domain.plan.repository;

import com.gmg.travice.domain.plan.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransportRepository extends JpaRepository<Transport, Integer> {

    Optional<Transport> findByTransportName(String transportName);
}
