package com.agritrade.agritrade.repository;

import com.agritrade.agritrade.Enum.GrainType;
import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.PurchaseGrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseGrainRepository extends JpaRepository<PurchaseGrain, Long> {
    List<PurchaseGrain> findByFarmer_FarmerId(Long farmerId);

    List<PurchaseGrain> findByGrainType(GrainType grainType);

    List<PurchaseGrain> findByStatus(Status status);

    List<PurchaseGrain> findByFarmer_FarmerIdAndGrainType(Long farmerId, GrainType grainType);

    @Query("SELECT p FROM PurchaseGrain p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<PurchaseGrain> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM PurchaseGrain p WHERE p.totalRemainGrainWeight > 0 AND p.status = 'ACTIVE'")
    List<PurchaseGrain> findAllWithRemainingStock();

    @Query("SELECT SUM(p.totalGrainWeight) FROM PurchaseGrain p WHERE p.farmer.farmerId = :farmerId")
    Double getTotalGrainWeightByFarmer(@Param("farmerId") Long farmerId);

    @Query("SELECT SUM(p.totalPrice) FROM PurchaseGrain p WHERE p.farmer.farmerId = :farmerId")
    Double getTotalPriceByFarmer(@Param("farmerId") Long farmerId);
}
