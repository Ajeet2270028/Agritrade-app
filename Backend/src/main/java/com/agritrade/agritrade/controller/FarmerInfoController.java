package com.agritrade.agritrade.controller;

import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.FarmerInfo;
import com.agritrade.agritrade.exception.ApiError;
import com.agritrade.agritrade.exception.DuplicateResourceException;
import com.agritrade.agritrade.exception.ResourceNotFoundException;
import com.agritrade.agritrade.interfaces.FarmerInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
@Slf4j
public class FarmerInfoController {
    private final FarmerInfoService farmerService;

    @PostMapping
    public ResponseEntity<?> createFarmer(@RequestBody FarmerInfo farmerInfo) {
        try {
            FarmerInfo created = farmerService.createFarmer(farmerInfo);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (DuplicateResourceException ex) {
            log.warn("Create farmer failed: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Unexpected error creating farmer: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to create farmer: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{farmerId}")
    public ResponseEntity<?> getFarmerById(@PathVariable Long farmerId) {
        try {
            return ResponseEntity.ok(farmerService.getFarmerById(farmerId));
        } catch (ResourceNotFoundException ex) {
            log.warn("Get farmer failed: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Unexpected error fetching farmer {}: {}", farmerId, ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to fetch farmer: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFarmers() {
        try {
            return ResponseEntity.ok(farmerService.getAllFarmers());
        } catch (Exception ex) {
            log.error("Unexpected error fetching farmers: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to fetch farmers: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{farmerId}")
    public ResponseEntity<?> updateFarmer(@PathVariable Long farmerId, @RequestBody FarmerInfo farmerInfo) {
        try {
            return ResponseEntity.ok(farmerService.updateFarmer(farmerId, farmerInfo));
        } catch (ResourceNotFoundException ex) {
            log.warn("Update farmer failed: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (DuplicateResourceException ex) {
            log.warn("Update farmer failed: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Unexpected error updating farmer {}: {}", farmerId, ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to update farmer: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{farmerId}")
    public ResponseEntity<?> deleteFarmer(@PathVariable Long farmerId) {
        try {
            farmerService.deleteFarmer(farmerId);
            return ResponseEntity.ok("Farmer deleted successfully");
        } catch (ResourceNotFoundException ex) {
            log.warn("Delete farmer failed: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Unexpected error deleting farmer {}: {}", farmerId, ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to delete farmer: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getFarmersByStatus(@PathVariable Status status) {
        try {
            return ResponseEntity.ok(farmerService.getFarmersByStatus(status));
        } catch (Exception ex) {
            log.error("Unexpected error fetching farmers by status {}: {}", status, ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to fetch farmers by status: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFarmers(@RequestParam String name) {
        try {
            return ResponseEntity.ok(farmerService.searchFarmersByName(name));
        } catch (Exception ex) {
            log.error("Unexpected error searching farmers by name {}: {}", name, ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to search farmers: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{farmerId}/status")
    public ResponseEntity<?> updateFarmerStatus(@PathVariable Long farmerId, @RequestParam Status status) {
        try {
            return ResponseEntity.ok(farmerService.updateFarmerStatus(farmerId, status));
        } catch (ResourceNotFoundException ex) {
            log.warn("Update farmer status failed: {}", ex.getMessage());
            return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Unexpected error updating farmer status {}: {}", farmerId, ex.getMessage());
            return new ResponseEntity<>(new ApiError("Failed to update farmer status: " + ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
