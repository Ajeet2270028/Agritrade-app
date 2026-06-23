package com.agritrade.agritrade.service;

import com.agritrade.agritrade.Enum.GrainType;
import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.FarmerInfo;
import com.agritrade.agritrade.entity.PurchaseGrain;
import com.agritrade.agritrade.exception.DuplicateResourceException;
import com.agritrade.agritrade.exception.ResourceNotFoundException;
import com.agritrade.agritrade.interfaces.PurchaseGrainService;
import com.agritrade.agritrade.repository.FarmerRepository;
import com.agritrade.agritrade.repository.PurchaseGrainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseGrainServiceImpl implements PurchaseGrainService {

    private final PurchaseGrainRepository purchaseGrainRepository;
    private final FarmerRepository farmerRepository;

    // ───────────────────────────── CREATE ─────────────────────────────

//    @Override
//    @Transactional
//    public PurchaseGrain createPurchase(Long farmerId, PurchaseGrain purchaseGrain) {
//        try {
//            FarmerInfo farmer = farmerRepository.findById(farmerId)
//                    .orElseThrow(() -> new ResourceNotFoundException(
//                            "Farmer not found with id: " + farmerId));
//
//            if (farmer.getStatus() != Status.ACTIVE) {
//                throw new IllegalStateException(
//                        "Cannot create purchase for inactive farmer with id: " + farmerId);
//            }
//
//            if (purchaseGrain.getGrainRatePerKg() == null || purchaseGrain.getGrainRatePerKg() <= 0) {
//                throw new IllegalArgumentException("Grain rate per kg must be greater than 0");
//            }
//
//            if (purchaseGrain.getGrainType() == null) {
//                throw new IllegalArgumentException("Grain type must not be null");
//            }
//
//            purchaseGrain.setFarmer(farmer);
//            purchaseGrain.setTotalBags(0L);
//            purchaseGrain.setTotalGrainWeight(0.0);
//            purchaseGrain.setTotalSoldWeight(0.0);
//            purchaseGrain.setTotalRemainGrainWeight(0.0);
//            purchaseGrain.setTotalPrice(0.0);
//            purchaseGrain.setStatus(Status.ACTIVE);
//
//            return purchaseGrainRepository.save(purchaseGrain);
//
//        } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException ex) {
//            log.warn("Create purchase failed: {}", ex.getMessage());
//            throw ex;
//        } catch (DataIntegrityViolationException ex) {
//            log.error("Database constraint violation while creating purchase: {}", ex.getMessage());
//            throw new DuplicateResourceException("Purchase with given details already exists");
//        } catch (Exception ex) {
//            log.error("Unexpected error while creating purchase: {}", ex.getMessage());
//            throw new RuntimeException("Failed to create purchase: " + ex.getMessage());
//        }
//    }

    @Override
    @Transactional
    public PurchaseGrain createPurchase(Long farmerId, PurchaseGrain purchaseGrain) {
        try {
            FarmerInfo farmer = farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Farmer not found with id: " + farmerId));

            if (farmer.getStatus() != Status.ACTIVE) {
                throw new IllegalStateException(
                        "Cannot create purchase for inactive farmer with id: " + farmerId);
            }

            if (purchaseGrain.getGrainRatePerKg() == null || purchaseGrain.getGrainRatePerKg() <= 0) {
                throw new IllegalArgumentException("Grain rate per kg must be greater than 0");
            }

            if (purchaseGrain.getGrainType() == null) {
                throw new IllegalArgumentException("Grain type must not be null");
            }

            // ✅ Always create a fresh entity — never use request body object directly
            PurchaseGrain newPurchase = new PurchaseGrain();
            newPurchase.setFarmer(farmer);
            newPurchase.setGrainType(purchaseGrain.getGrainType());
            newPurchase.setGrainRatePerKg(purchaseGrain.getGrainRatePerKg());
            newPurchase.setBagWeights(new ArrayList<>());
            newPurchase.setTotalBags(0L);
            newPurchase.setTotalGrainWeight(0.0);
            newPurchase.setTotalSoldWeight(0.0);
            newPurchase.setTotalRemainGrainWeight(0.0);
            newPurchase.setTotalPrice(0.0);
            newPurchase.setStatus(Status.ACTIVE);

            return purchaseGrainRepository.save(newPurchase);

        } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException ex) {
            log.warn("Create purchase failed: {}", ex.getMessage());
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            log.error("Database constraint violation while creating purchase: {}", ex.getMessage());
            throw new DuplicateResourceException("Purchase with given details already exists");
        } catch (Exception ex) {
            log.error("Unexpected error while creating purchase: {}", ex.getMessage());
            throw new RuntimeException("Failed to create purchase: " + ex.getMessage());
        }
    }

    // ───────────────────────────── READ ─────────────────────────────

    @Override
    public PurchaseGrain getPurchaseById(Long purchaseId) {
        try {
            return purchaseGrainRepository.findById(purchaseId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Purchase not found with id: " + purchaseId));
        } catch (ResourceNotFoundException ex) {
            log.warn("Get purchase failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error fetching purchase {}: {}", purchaseId, ex.getMessage());
            throw new RuntimeException("Failed to fetch purchase: " + ex.getMessage());
        }
    }

    @Override
    public List<PurchaseGrain> getAllPurchases() {
        try {
            return purchaseGrainRepository.findAll();
        } catch (Exception ex) {
            log.error("Unexpected error fetching all purchases: {}", ex.getMessage());
            throw new RuntimeException("Failed to fetch purchases: " + ex.getMessage());
        }
    }

    @Override
    public List<PurchaseGrain> getPurchasesByFarmer(Long farmerId) {
        try {
            farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Farmer not found with id: " + farmerId));
            return purchaseGrainRepository.findByFarmer_FarmerId(farmerId);
        } catch (ResourceNotFoundException ex) {
            log.warn("Get purchases by farmer failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error fetching purchases for farmer {}: {}", farmerId, ex.getMessage());
            throw new RuntimeException("Failed to fetch purchases by farmer: " + ex.getMessage());
        }
    }

    @Override
    public List<PurchaseGrain> getPurchasesByGrainType(GrainType grainType) {
        try {
            if (grainType == null) {
                throw new IllegalArgumentException("Grain type must not be null");
            }
            return purchaseGrainRepository.findByGrainType(grainType);
        } catch (IllegalArgumentException ex) {
            log.warn("Get purchases by grain type failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error fetching purchases by grain type {}: {}", grainType, ex.getMessage());
            throw new RuntimeException("Failed to fetch purchases by grain type: " + ex.getMessage());
        }
    }



    @Override
    public List<PurchaseGrain> getPurchasesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Start date and end date must not be null");
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must not be after end date");
            }
            return purchaseGrainRepository.findByDateRange(startDate, endDate);
        } catch (IllegalArgumentException ex) {
            log.warn("Get purchases by date range failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error fetching purchases by date range: {}", ex.getMessage());
            throw new RuntimeException("Failed to fetch purchases by date range: " + ex.getMessage());
        }
    }

    @Override
    public List<PurchaseGrain> getPurchasesWithRemainingStock() {
        try {
            return purchaseGrainRepository.findAllWithRemainingStock();
        } catch (Exception ex) {
            log.error("Unexpected error fetching purchases with remaining stock: {}", ex.getMessage());
            throw new RuntimeException("Failed to fetch purchases with remaining stock: " + ex.getMessage());
        }
    }

    // ───────────────────────────── UPDATE ─────────────────────────────

    @Override
    @Transactional
    public PurchaseGrain updatePurchase(Long purchaseId, PurchaseGrain updatedPurchase) {
        try {
            PurchaseGrain existing = purchaseGrainRepository.findById(purchaseId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Purchase not found with id: " + purchaseId));

            if (updatedPurchase.getGrainRatePerKg() != null && updatedPurchase.getGrainRatePerKg() <= 0) {
                throw new IllegalArgumentException("Grain rate per kg must be greater than 0");
            }

            if (updatedPurchase.getGrainType() != null) {
                existing.setGrainType(updatedPurchase.getGrainType());
            }
            if (updatedPurchase.getGrainRatePerKg() != null) {
                existing.setGrainRatePerKg(updatedPurchase.getGrainRatePerKg());
                // recalculate total price with new rate
                existing.setTotalPrice(existing.getTotalGrainWeight() * updatedPurchase.getGrainRatePerKg());
            }
            if (updatedPurchase.getStatus() != null) {
                existing.setStatus(updatedPurchase.getStatus());
            }

            return purchaseGrainRepository.save(existing);

        } catch (ResourceNotFoundException | IllegalArgumentException ex) {
            log.warn("Update purchase failed: {}", ex.getMessage());
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            log.error("Database constraint violation while updating purchase: {}", ex.getMessage());
            throw new DuplicateResourceException("Purchase update violates data constraints");
        } catch (Exception ex) {
            log.error("Unexpected error updating purchase {}: {}", purchaseId, ex.getMessage());
            throw new RuntimeException("Failed to update purchase: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public PurchaseGrain addBagWeight(Long purchaseId, Double weight) {
        try {
            if (weight == null || weight <= 0) {
                throw new IllegalArgumentException("Bag weight must be greater than 0");
            }

            PurchaseGrain purchase = purchaseGrainRepository.findById(purchaseId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Purchase not found with id: " + purchaseId));

            if (purchase.getStatus() != Status.ACTIVE) {
                throw new IllegalStateException(
                        "Cannot add bag to inactive purchase with id: " + purchaseId);
            }

            purchase.addBagWeight(weight);   // entity helper recalculates all totals
            return purchaseGrainRepository.save(purchase);

        } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException ex) {
            log.warn("Add bag weight failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error adding bag weight to purchase {}: {}", purchaseId, ex.getMessage());
            throw new RuntimeException("Failed to add bag weight: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public PurchaseGrain removeBagWeight(Long purchaseId, int bagIndex) {
        try {
            PurchaseGrain purchase = purchaseGrainRepository.findById(purchaseId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Purchase not found with id: " + purchaseId));

            if (purchase.getBagWeights().isEmpty()) {
                throw new IllegalStateException("No bags exist for purchase with id: " + purchaseId);
            }
            if (bagIndex < 0 || bagIndex >= purchase.getBagWeights().size()) {
                throw new IllegalArgumentException(
                        "Invalid bag index " + bagIndex + ". Valid range: 0 to "
                                + (purchase.getBagWeights().size() - 1));
            }

            purchase.getBagWeights().remove(bagIndex);

            // recalculate all totals after removal
            double newTotal = purchase.getBagWeights().stream()
                    .mapToDouble(Double::doubleValue).sum();
            purchase.setTotalBags((long) purchase.getBagWeights().size());
            purchase.setTotalGrainWeight(newTotal);
            purchase.setTotalRemainGrainWeight(newTotal - purchase.getTotalSoldWeight());
            purchase.setTotalPrice(newTotal * purchase.getGrainRatePerKg());

            return purchaseGrainRepository.save(purchase);

        } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException ex) {
            log.warn("Remove bag weight failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error removing bag weight from purchase {}: {}", purchaseId, ex.getMessage());
            throw new RuntimeException("Failed to remove bag weight: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public PurchaseGrain updateSoldWeight(Long purchaseId, Double soldWeight) {
        try {
            if (soldWeight == null || soldWeight < 0) {
                throw new IllegalArgumentException("Sold weight must be 0 or greater");
            }

            PurchaseGrain purchase = purchaseGrainRepository.findById(purchaseId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Purchase not found with id: " + purchaseId));

            if (soldWeight > purchase.getTotalGrainWeight()) {
                throw new IllegalArgumentException(
                        "Sold weight (" + soldWeight + " kg) cannot exceed total grain weight ("
                                + purchase.getTotalGrainWeight() + " kg)");
            }

            purchase.updateSoldWeight(soldWeight);   // entity helper updates remaining weight
            return purchaseGrainRepository.save(purchase);

        } catch (ResourceNotFoundException | IllegalArgumentException ex) {
            log.warn("Update sold weight failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error updating sold weight for purchase {}: {}", purchaseId, ex.getMessage());
            throw new RuntimeException("Failed to update sold weight: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public PurchaseGrain updateStatus(Long purchaseId, Status status) {
        try {
            if (status == null) {
                throw new IllegalArgumentException("Status must not be null");
            }
            PurchaseGrain purchase = purchaseGrainRepository.findById(purchaseId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Purchase not found with id: " + purchaseId));
            purchase.setStatus(status);
            return purchaseGrainRepository.save(purchase);
        } catch (ResourceNotFoundException | IllegalArgumentException ex) {
            log.warn("Update purchase status failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error updating status for purchase {}: {}", purchaseId, ex.getMessage());
            throw new RuntimeException("Failed to update purchase status: " + ex.getMessage());
        }
    }

    // ───────────────────────────── DELETE ─────────────────────────────

    @Override
    @Transactional
    public void deletePurchase(Long purchaseId) {
        try {
            PurchaseGrain purchase = purchaseGrainRepository.findById(purchaseId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Purchase not found with id: " + purchaseId));
            purchaseGrainRepository.delete(purchase);
        } catch (ResourceNotFoundException ex) {
            log.warn("Delete purchase failed: {}", ex.getMessage());
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            log.error("Cannot delete purchase {} due to existing references: {}", purchaseId, ex.getMessage());
            throw new IllegalStateException(
                    "Cannot delete purchase as it is referenced by other records");
        } catch (Exception ex) {
            log.error("Unexpected error deleting purchase {}: {}", purchaseId, ex.getMessage());
            throw new RuntimeException("Failed to delete purchase: " + ex.getMessage());
        }
    }

    // ───────────────────────────── AGGREGATES ─────────────────────────────

    @Override
    public Double getTotalGrainWeightByFarmer(Long farmerId) {
        try {
            farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Farmer not found with id: " + farmerId));
            Double result = purchaseGrainRepository.getTotalGrainWeightByFarmer(farmerId);
            return result != null ? result : 0.0;
        } catch (ResourceNotFoundException ex) {
            log.warn("Get total grain weight failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error fetching total grain weight for farmer {}: {}", farmerId, ex.getMessage());
            throw new RuntimeException("Failed to fetch total grain weight: " + ex.getMessage());
        }
    }

    @Override
    public Double getTotalPriceByFarmer(Long farmerId) {
        try {
            farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Farmer not found with id: " + farmerId));
            Double result = purchaseGrainRepository.getTotalPriceByFarmer(farmerId);
            return result != null ? result : 0.0;
        } catch (ResourceNotFoundException ex) {
            log.warn("Get total price failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error fetching total price for farmer {}: {}", farmerId, ex.getMessage());
            throw new RuntimeException("Failed to fetch total price: " + ex.getMessage());
        }
    }
}
