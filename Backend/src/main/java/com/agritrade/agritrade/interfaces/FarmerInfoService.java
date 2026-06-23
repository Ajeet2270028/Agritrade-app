package com.agritrade.agritrade.interfaces;

import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.FarmerInfo;

import java.util.List;

public interface FarmerInfoService {
    FarmerInfo createFarmer(FarmerInfo farmerInfo);

    FarmerInfo getFarmerById(Long farmerId);

    List<FarmerInfo> getAllFarmers();

    FarmerInfo updateFarmer(Long farmerId, FarmerInfo farmerInfo);

    void deleteFarmer(Long farmerId);

    List<FarmerInfo> getFarmersByStatus(Status status);

    List<FarmerInfo> searchFarmersByName(String name);

    FarmerInfo updateFarmerStatus(Long farmerId, Status status);

}
