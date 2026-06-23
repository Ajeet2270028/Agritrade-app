package com.agritrade.agritrade.interfaces;

import com.agritrade.agritrade.Enum.GrainType;
import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.PurchaseGrain;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseGrainService {
    PurchaseGrain createPurchase(Long farmerId, PurchaseGrain purchaseGrain);

    PurchaseGrain getPurchaseById(Long purchaseId);

    List<PurchaseGrain> getAllPurchases();

    PurchaseGrain updatePurchase(Long purchaseId, PurchaseGrain purchaseGrain);

    void deletePurchase(Long purchaseId);

    PurchaseGrain addBagWeight(Long purchaseId, Double weight);

    PurchaseGrain removeBagWeight(Long purchaseId, int bagIndex);

    PurchaseGrain updateSoldWeight(Long purchaseId, Double soldWeight);

    PurchaseGrain updateStatus(Long purchaseId, Status status);

    List<PurchaseGrain> getPurchasesByFarmer(Long farmerId);

    List<PurchaseGrain> getPurchasesByGrainType(GrainType grainType);

    List<PurchaseGrain> getPurchasesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<PurchaseGrain> getPurchasesWithRemainingStock();

    Double getTotalGrainWeightByFarmer(Long farmerId);

    Double getTotalPriceByFarmer(Long farmerId);
}
