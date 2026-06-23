package com.agritrade.agritrade.controller;

import com.agritrade.agritrade.Enum.GrainType;
import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.PurchaseGrain;
import com.agritrade.agritrade.exception.ApiError;
import com.agritrade.agritrade.exception.DuplicateResourceException;
import com.agritrade.agritrade.exception.ResourceNotFoundException;
import com.agritrade.agritrade.interfaces.PurchaseGrainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@Slf4j
public class PurchaseGrainController {

    private final PurchaseGrainService purchaseGrainService;

    // ───────── CREATE ─────────

    @PostMapping("/farmer/{farmerId}")
    public ResponseEntity<?> createPurchase(@PathVariable Long farmerId,
                                            @RequestBody PurchaseGrain purchaseGrain) {
        try {
            PurchaseGrain created = purchaseGrainService.createPurchase(farmerId, purchaseGrain);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);
        } catch (DuplicateResourceException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to create purchase: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ───────── READ ─────────

    @GetMapping("/{purchaseId}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Long purchaseId) {
        try {
            return ResponseEntity.ok(purchaseGrainService.getPurchaseById(purchaseId));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch purchase: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPurchases() {
        try {
            return ResponseEntity.ok(purchaseGrainService.getAllPurchases());
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch purchases: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<?> getPurchasesByFarmer(@PathVariable Long farmerId) {
        try {
            return ResponseEntity.ok(purchaseGrainService.getPurchasesByFarmer(farmerId));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch farmer purchases: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/grain-type/{grainType}")
    public ResponseEntity<?> getPurchasesByGrainType(@PathVariable GrainType grainType) {
        try {
            return ResponseEntity.ok(purchaseGrainService.getPurchasesByGrainType(grainType));
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch purchases by grain type: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<?> getPurchasesByDateRange(@RequestParam LocalDateTime startDate,
                                                     @RequestParam LocalDateTime endDate) {
        try {
            return ResponseEntity.ok(purchaseGrainService.getPurchasesByDateRange(startDate, endDate));
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch purchases by date range: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/remaining-stock")
    public ResponseEntity<?> getPurchasesWithRemainingStock() {
        try {
            return ResponseEntity.ok(purchaseGrainService.getPurchasesWithRemainingStock());
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch remaining stock: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ───────── UPDATE ─────────

    @PutMapping("/{purchaseId}")
    public ResponseEntity<?> updatePurchase(@PathVariable Long purchaseId,
                                            @RequestBody PurchaseGrain purchaseGrain) {
        try {
            return ResponseEntity.ok(purchaseGrainService.updatePurchase(purchaseId, purchaseGrain));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (DuplicateResourceException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to update purchase: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{purchaseId}/add-bag")
    public ResponseEntity<?> addBagWeight(@PathVariable Long purchaseId,
                                          @RequestParam Double weight) {
        try {
            return ResponseEntity.ok(purchaseGrainService.addBagWeight(purchaseId, weight));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to add bag weight: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{purchaseId}/remove-bag")
    public ResponseEntity<?> removeBagWeight(@PathVariable Long purchaseId,
                                             @RequestParam int bagIndex) {
        try {
            return ResponseEntity.ok(purchaseGrainService.removeBagWeight(purchaseId, bagIndex));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to remove bag weight: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{purchaseId}/sold-weight")
    public ResponseEntity<?> updateSoldWeight(@PathVariable Long purchaseId,
                                              @RequestParam Double soldWeight) {
        try {
            return ResponseEntity.ok(purchaseGrainService.updateSoldWeight(purchaseId, soldWeight));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to update sold weight: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{purchaseId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long purchaseId,
                                          @RequestParam Status status) {
        try {
            return ResponseEntity.ok(purchaseGrainService.updateStatus(purchaseId, status));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to update status: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ───────── DELETE ─────────

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<?> deletePurchase(@PathVariable Long purchaseId) {
        try {
            purchaseGrainService.deletePurchase(purchaseId);
            return ResponseEntity.ok("Purchase deleted successfully");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT),
                    HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to delete purchase: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ───────── AGGREGATES ─────────

    @GetMapping("/farmer/{farmerId}/total-weight")
    public ResponseEntity<?> getTotalGrainWeightByFarmer(@PathVariable Long farmerId) {
        try {
            return ResponseEntity.ok(purchaseGrainService.getTotalGrainWeightByFarmer(farmerId));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch total grain weight: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/farmer/{farmerId}/total-price")
    public ResponseEntity<?> getTotalPriceByFarmer(@PathVariable Long farmerId) {
        try {
            return ResponseEntity.ok(purchaseGrainService.getTotalPriceByFarmer(farmerId));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiError("Failed to fetch total price: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
