package com.gmg.travice.domain.plan.repository;

import com.gmg.travice.domain.plan.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    // region 문자열로 City 찾기 (정확히 일치)
    Optional<City> findByCityName(String cityName);

    // region 문자열을 포함하는 City 찾기 (부분 매칭)
    @Query("SELECT c FROM City c WHERE c.cityName LIKE %:region%")
    Optional<City> findByCityNameContaining(@Param("region") String region);

    // 여러 조건으로 찾기 (더 유연한 매칭)
    @Query("SELECT c FROM City c WHERE c.cityName = :region OR c.cityName LIKE %:region%")
    Optional<City> findByCityNameOrContaining(@Param("region") String region);
}