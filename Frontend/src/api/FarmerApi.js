import API from './AxiosConfig';

// Create Farmer
export const createFarmer = (farmer) =>
    API.post("/farmers", farmer);

// Get All Farmers
export const getAllFarmers = () =>
    API.get("/farmers");

// Get Farmer By Id
export const getFarmerById = (id) =>
    API.get(`/farmers/${id}`);

// Update Farmer
export const updateFarmer = (id, farmer) =>
    API.put(`/farmers/${id}`, farmer);

// Delete Farmer
export const deleteFarmer = (id) =>
    API.delete(`/farmers/${id}`);

// Search Farmer
export const searchFarmer = (name) =>
    API.get(`/farmers/search?name=${name}`);

// Status Wise Farmer
export const getFarmerByStatus = (status) =>
    API.get(`/farmers/status/${status}`);

// Update Status
export const updateStatus = (id, status) =>
    API.patch(`/farmers/${id}/status?status=${status}`);
