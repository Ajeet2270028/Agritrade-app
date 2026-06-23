package com.agritrade.agritrade.service;

import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.FarmerInfo;
import com.agritrade.agritrade.exception.DuplicateResourceException;
import com.agritrade.agritrade.exception.ResourceNotFoundException;
import com.agritrade.agritrade.interfaces.FarmerInfoService;
import com.agritrade.agritrade.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmerInfoImp implements FarmerInfoService {
    private final FarmerRepository farmerRepository;

    @Override
    public FarmerInfo createFarmer(FarmerInfo farmerInfo) {
        try {
            if (farmerInfo.getAadhaarNumber() != null
                    && farmerRepository.existsByAadhaarNumber(farmerInfo.getAadhaarNumber())) {
                throw new DuplicateResourceException(
                        "A farmer with Aadhaar number " + farmerInfo.getAadhaarNumber() + " already exists");
            }
            if (farmerInfo.getPhone() != null
                    && farmerRepository.existsByPhone(farmerInfo.getPhone())) {
                throw new DuplicateResourceException(
                        "A farmer with phone number " + farmerInfo.getPhone() + " already exists");
            }
            return farmerRepository.save(farmerInfo);

        } catch (DuplicateResourceException ex) {
            log.warn("Duplicate farmer creation attempt: {}", ex.getMessage());
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            log.error("Database constraint violation while creating farmer: {}", ex.getMessage());
            throw new DuplicateResourceException("Farmer with given details already exists");
        } catch (Exception ex) {
            log.error("Unexpected error while creating farmer: {}", ex.getMessage());
            throw new RuntimeException("Failed to create farmer: " + ex.getMessage());
        }
    }

    @Override
    public FarmerInfo getFarmerById(Long farmerId) {
        try {
            return farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + farmerId));
        } catch (ResourceNotFoundException ex) {
            log.warn("Farmer lookup failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while fetching farmer with id {}: {}", farmerId, ex.getMessage());
            throw new RuntimeException("Failed to fetch farmer: " + ex.getMessage());
        }
    }

    @Override
    public List<FarmerInfo> getAllFarmers() {
        try {
            return farmerRepository.findAll();
        } catch (Exception ex) {
            log.error("Unexpected error while fetching farmers: {}", ex.getMessage());
            throw new RuntimeException("Failed to fetch farmers: " + ex.getMessage());
        }
    }

    @Override
    public FarmerInfo updateFarmer(Long farmerId, FarmerInfo updatedFarmer) {
        try {
            FarmerInfo existingFarmer = farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + farmerId));

            if (updatedFarmer.getAadhaarNumber() != null
                    && !updatedFarmer.getAadhaarNumber().equals(existingFarmer.getAadhaarNumber())
                    && farmerRepository.existsByAadhaarNumber(updatedFarmer.getAadhaarNumber())) {
                throw new DuplicateResourceException(
                        "A farmer with Aadhaar number " + updatedFarmer.getAadhaarNumber() + " already exists");
            }

            existingFarmer.setFarmerName(updatedFarmer.getFarmerName());
            existingFarmer.setPhone(updatedFarmer.getPhone());
            existingFarmer.setVillage(updatedFarmer.getVillage());
            existingFarmer.setDistrict(updatedFarmer.getDistrict());
            existingFarmer.setState(updatedFarmer.getState());
            existingFarmer.setAadhaarNumber(updatedFarmer.getAadhaarNumber());
            existingFarmer.setBankAccount(updatedFarmer.getBankAccount());
            existingFarmer.setIfscCode(updatedFarmer.getIfscCode());
            if (updatedFarmer.getStatus() != null) {
                existingFarmer.setStatus(updatedFarmer.getStatus());
            }

            return farmerRepository.save(existingFarmer);

        } catch (ResourceNotFoundException | DuplicateResourceException ex) {
            log.warn("Farmer update failed: {}", ex.getMessage());
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            log.error("Database constraint violation while updating farmer: {}", ex.getMessage());
            throw new DuplicateResourceException("Farmer with given details already exists");
        } catch (Exception ex) {
            log.error("Unexpected error while updating farmer with id {}: {}", farmerId, ex.getMessage());
            throw new RuntimeException("Failed to update farmer: " + ex.getMessage());
        }
    }

    @Override
    public void deleteFarmer(Long farmerId) {
        try {
            FarmerInfo farmer = farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + farmerId));
            farmerRepository.delete(farmer);
        } catch (ResourceNotFoundException ex) {
            log.warn("Farmer deletion failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while deleting farmer with id {}: {}", farmerId, ex.getMessage());
            throw new RuntimeException("Failed to delete farmer: " + ex.getMessage());
        }
    }

    @Override
    public List<FarmerInfo> getFarmersByStatus(Status status) {
        try {
            return farmerRepository.findByStatus(status);
        } catch (Exception ex) {
            log.error("Unexpected error while fetching farmers by status {}: {}", status, ex.getMessage());
            throw new RuntimeException("Failed to fetch farmers by status: " + ex.getMessage());
        }
    }

    @Override
    public List<FarmerInfo> searchFarmersByName(String name) {
        try {
            return farmerRepository.searchByName(name);
        } catch (Exception ex) {
            log.error("Unexpected error while searching farmers by name {}: {}", name, ex.getMessage());
            throw new RuntimeException("Failed to search farmers: " + ex.getMessage());
        }
    }

    @Override
    public FarmerInfo updateFarmerStatus(Long farmerId, Status status) {
        try {
            FarmerInfo farmer = farmerRepository.findById(farmerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + farmerId));
            farmer.setStatus(status);
            return farmerRepository.save(farmer);
        } catch (ResourceNotFoundException ex) {
            log.warn("Farmer status update failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while updating farmer status for id {}: {}", farmerId, ex.getMessage());
            throw new RuntimeException("Failed to update farmer status: " + ex.getMessage());
        }
    }
}
