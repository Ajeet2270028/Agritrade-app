package com.agritrade.agritrade.repository;

import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.FarmerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FarmerRepository extends JpaRepository<FarmerInfo, Long> {

    Optional<FarmerInfo> findByAadhaarNumber(String aadhaarNumber);

    Optional<FarmerInfo> findByPhone(String phone);

    boolean existsByAadhaarNumber(String aadhaarNumber);

    boolean existsByPhone(String phone);

    List<FarmerInfo> findByStatus(Status status);

    List<FarmerInfo> findByDistrictAndState(String district, String state);

    @Query("SELECT f FROM FarmerInfo f WHERE LOWER(f.farmerName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FarmerInfo> searchByName(@Param("name") String name);
}
