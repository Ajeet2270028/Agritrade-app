import React, { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import {
  getAllFarmers,
  deleteFarmer,
  searchFarmer,
  getFarmerByStatus,
  updateStatus,
} from "../api/farmerApi"; // adjust path to where farmerApi.js lives
import "../style/Farmer.css";

const Farmer = () => {
  const navigate = useNavigate();
  const [farmers, setFarmers] = useState([]);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Stable reference (empty deps) so effects that depend on it don't
  // re-fire on every render and re-trigger backend queries.
  const fetchFarmers = useCallback(async (term, status) => {
    setLoading(true);
    setError("");
    try {
      let res;
      if (term && term.trim()) {
        res = await searchFarmer(term.trim());
      } else if (status && status !== "ALL") {
        res = await getFarmerByStatus(status);
      } else {
        res = await getAllFarmers();
      }
      setFarmers(res.data || []);
    } catch (err) {
      setError(err?.response?.data?.message || "Failed to load farmers");
    } finally {
      setLoading(false);
    }
  }, []);

  // Debounce search/status changes so we don't fire a request on every
  // keystroke.
  useEffect(() => {
    const handle = setTimeout(() => {
      fetchFarmers(search, statusFilter);
    }, 300);
    return () => clearTimeout(handle);
  }, [search, statusFilter, fetchFarmers]);

  const handleCreate = () => {
    navigate("/farmer/create-farmer");
  };

  const handleEdit = (farmer) => {
    navigate(`/farmer/edit-farmer/${farmer.farmerId}`);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this farmer record?")) return;
    try {
      await deleteFarmer(id);
      setFarmers((prev) => prev.filter((f) => f.farmerId !== id));
    } catch (err) {
      setError(err?.response?.data?.message || "Failed to delete farmer");
    }
  };

  const handleToggleStatus = async (farmer) => {
    const newStatus = farmer.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    try {
      await updateStatus(farmer.farmerId, newStatus);
      setFarmers((prev) =>
        prev.map((f) => (f.farmerId === farmer.farmerId ? { ...f, status: newStatus } : f)),
      );
    } catch (err) {
      setError(err?.response?.data?.message || "Failed to update status");
    }
  };

  return (
    <div className="farmer-page">
      <div className="farmer-page-header">
        <h1 className="farmer-page-title">Farmers</h1>
        <button onClick={handleCreate} className="btn btn-primary">
          + Add Farmer
        </button>
      </div>

      <div className="farmer-toolbar">
        <input
          type="text"
          placeholder="Search by name..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="search-input"
        />
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          className="status-filter"
        >
          <option value="ALL">All Status</option>
          <option value="ACTIVE">Active</option>
          <option value="INACTIVE">Inactive</option>
        </select>
      </div>

      {error && <p className="farmer-error">{error}</p>}

      <div className="farmer-table-wrap">
        <table className="farmer-table">
          <thead>
            <tr>
              <th>Farmer Name</th>
              <th>Phone</th>
              <th>Village</th>
              <th>District</th>
              <th>State</th>
              <th>Aadhaar No</th>
              <th>Account</th>
              <th>IFSC Code</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan={10} className="farmer-table-empty">
                  Loading farmers...
                </td>
              </tr>
            ) : farmers.length === 0 ? (
              <tr>
                <td colSpan={10} className="farmer-table-empty">
                  No farmers found.
                </td>
              </tr>
            ) : (
              farmers.map((f) => (
                <tr key={f.farmerId}>
                  <td>{f.farmerName}</td>
                  <td>{f.phone}</td>
                  <td>{f.village}</td>
                  <td>{f.district}</td>
                  <td>{f.state}</td>
                  <td>{f.aadhaarNumber}</td>
                  <td>{f.bankAccount}</td>
                  <td>{f.ifscCode}</td>
                  <td>
                    <button
                      onClick={() => handleToggleStatus(f)}
                      className={`status-badge status-toggle ${
                        f.status === "ACTIVE" ? "status-active" : "status-inactive"
                      }`}
                      title="Click to toggle status"
                    >
                      {f.status}
                    </button>
                  </td>
                  <td className="farmer-actions">
                    <button onClick={() => handleEdit(f)} className="link-btn link-edit">
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(f.farmerId)}
                      className="link-btn link-delete"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Farmer;