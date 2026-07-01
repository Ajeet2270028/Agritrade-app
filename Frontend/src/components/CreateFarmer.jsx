import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createFarmer, updateFarmer, getFarmerById } from "../api/farmerApi"; // adjust path to where farmerApi.js lives
import "../style/CreateFarmer.css";

const STATES = [
  "Andhra Pradesh",
  "Arunachal Pradesh",
  "Assam",
  "Bihar",
  "Chhattisgarh",
  "Goa",
  "Gujarat",
  "Haryana",
  "Himachal Pradesh",
  "Jharkhand",
  "Karnataka",
  "Kerala",
  "Madhya Pradesh",
  "Maharashtra",
  "Manipur",
  "Meghalaya",
  "Mizoram",
  "Nagaland",
  "Odisha",
  "Punjab",
  "Rajasthan",
  "Sikkim",
  "Tamil Nadu",
  "Telangana",
  "Tripura",
  "Uttar Pradesh",
  "Uttarakhand",
  "West Bengal",
];

const formatAadhaar = (value) => {
  const digits = value.replace(/\D/g, "").substring(0, 12);
  const parts = [];
  for (let i = 0; i < digits.length; i += 4) {
    parts.push(digits.slice(i, i + 4));
  }
  return parts.join("-");
};

const validators = {
  farmerName: (v) => {
    if (!v.trim()) return "Full name is required";
    if (v.trim().length < 3) return "Name must be at least 3 characters";
    if (!/^[a-zA-Z\s]+$/.test(v)) return "Only letters and spaces allowed";
    return "";
  },
  phone: (v) => {
    if (!v.trim()) return "Phone number is required";
    if (!/^[6-9]\d{9}$/.test(v)) return "Enter a valid 10-digit Indian mobile number";
    return "";
  },
  aadhaarNumber: (v) => {
    const digits = v.replace(/\D/g, "");
    if (!digits) return "Aadhaar number is required";
    if (digits.length !== 12) return "Aadhaar number must be 12 digits";
    return "";
  },
  village: (v) => {
    if (!v.trim()) return "Village is required";
    if (v.trim().length < 2) return "Village name must be at least 2 characters";
    return "";
  },
  district: (v) => {
    if (!v.trim()) return "District is required";
    if (v.trim().length < 2) return "District name must be at least 2 characters";
    return "";
  },
  state: (v) => (!v ? "Please select a state" : ""),
  bankAccount: (v) => {
    if (!v.trim()) return "Account number is required";
    if (!/^\d{9,18}$/.test(v)) return "Enter a valid 9–18 digit account number";
    return "";
  },
  ifscCode: (v) => {
    if (!v.trim()) return "IFSC code is required";
    if (!/^[A-Z]{4}0[A-Z0-9]{6}$/.test(v)) return "Enter valid IFSC (e.g. SBIN0001234)";
    return "";
  },
};

const initialForm = {
  farmerName: "",
  phone: "",
  village: "",
  district: "",
  state: "",
  aadhaarNumber: "",
  bankAccount: "",
  ifscCode: "",
  status: "ACTIVE",
};

const inputClass = (touched, error) =>
  `form-input ${touched && error ? "form-input-error" : ""}`;

const SectionLabel = ({ children }) => (
  <h3 className="section-label">{children}</h3>
);

const InputField = ({ label, id, required, error, touched, hint, children }) => (
  <div className="form-field">
    <label htmlFor={id} className="form-label">
      {label}
      {required && <span className="required-mark"> *</span>}
    </label>
    {children}
    {hint && !error && <span className="form-hint">{hint}</span>}
    {touched && error && <span className="form-error">{error}</span>}
  </div>
);

const StatusBadge = ({ status }) => (
  <span className={`status-badge ${status === "ACTIVE" ? "status-active" : "status-inactive"}`}>
    {status}
  </span>
);

/**
 * CreateFarmer
 * Routed at /farmer/create-farmer (create) and /farmer/edit-farmer/:id (edit).
 * Fetches the farmer record itself when an id is present, and calls
 * createFarmer / updateFarmer from farmerApi directly, then navigates
 * back to the farmer list.
 */
const CreateFarmer = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEdit = Boolean(id);

  const [form, setForm] = useState(initialForm);
  const [touched, setTouched] = useState({});
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(isEdit);
  const [submitError, setSubmitError] = useState("");

  useEffect(() => {
    if (!isEdit) return;
    let active = true;
    setFetching(true);
    getFarmerById(id)
      .then((res) => {
        if (active) setForm(res.data);
      })
      .catch((err) => {
        if (active) {
          setSubmitError(
            err?.response?.data?.message || "Failed to load farmer details",
          );
        }
      })
      .finally(() => {
        if (active) setFetching(false);
      });
    return () => {
      active = false;
    };
  }, [id, isEdit]);

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
    if (validators[field]) {
      setErrors((prev) => ({ ...prev, [field]: validators[field](value) }));
    }
  };

  const handleBlur = (field) => {
    setTouched((prev) => ({ ...prev, [field]: true }));
    if (validators[field]) {
      setErrors((prev) => ({ ...prev, [field]: validators[field](form[field]) }));
    }
  };

  const validateAll = () => {
    const newErrors = {};
    Object.keys(validators).forEach((field) => {
      newErrors[field] = validators[field](form[field] ?? "");
    });
    setErrors(newErrors);
    setTouched(
      Object.keys(validators).reduce((acc, f) => ({ ...acc, [f]: true }), {}),
    );
    return Object.values(newErrors).every((e) => !e);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitError("");
    if (!validateAll()) return;

    setLoading(true);
    try {
      const payload = {
        ...form,
        aadhaarNumber: form.aadhaarNumber.replace(/\D/g, ""),
      };
      if (isEdit) {
        await updateFarmer(id, payload);
      } else {
        await createFarmer(payload);
      }
      navigate("/farmer");
    } catch (err) {
      setSubmitError(
        err?.response?.data?.message || "Something went wrong. Please try again.",
      );
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate("/farmer");
  };

  if (fetching) {
    return <div className="create-farmer">Loading farmer details...</div>;
  }

  return (
    <div className="create-farmer">
      <form onSubmit={handleSubmit}>
        <SectionLabel>Farmer Details</SectionLabel>
        <div className="form-grid form-grid-3">
          <InputField
            label="Full Name"
            id="farmerName"
            required
            error={errors.farmerName}
            touched={touched.farmerName}
          >
            <input
              type="text"
              id="farmerName"
              maxLength={60}
              placeholder="e.g. Ram Kumar"
              value={form.farmerName}
              onChange={(e) => handleChange("farmerName", e.target.value)}
              onBlur={() => handleBlur("farmerName")}
              className={inputClass(touched.farmerName, errors.farmerName)}
            />
          </InputField>

          <InputField
            label="Phone"
            id="phone"
            required
            error={errors.phone}
            touched={touched.phone}
          >
            <input
              type="tel"
              placeholder="10 digit phone no"
              id="phone"
              maxLength={10}
              value={form.phone}
              onChange={(e) =>
                handleChange("phone", e.target.value.replace(/\D/g, "").slice(0, 10))
              }
              onBlur={() => handleBlur("phone")}
              className={inputClass(touched.phone, errors.phone)}
            />
          </InputField>

          <InputField
            label="Aadhaar Number"
            id="aadhaarNumber"
            required
            error={errors.aadhaarNumber}
            touched={touched.aadhaarNumber}
          >
            <input
              type="text"
              inputMode="numeric"
              placeholder="1234-5678-9012"
              id="aadhaarNumber"
              maxLength={14}
              value={form.aadhaarNumber}
              onChange={(e) =>
                handleChange("aadhaarNumber", formatAadhaar(e.target.value))
              }
              onBlur={() => handleBlur("aadhaarNumber")}
              className={inputClass(touched.aadhaarNumber, errors.aadhaarNumber)}
            />
          </InputField>
        </div>

        <SectionLabel>Address</SectionLabel>
        <div className="form-grid form-grid-3">
          <InputField
            label="Village"
            id="village"
            required
            error={errors.village}
            touched={touched.village}
          >
            <input
              type="text"
              placeholder="Enter Village"
              maxLength={60}
              id="village"
              value={form.village}
              onChange={(e) => handleChange("village", e.target.value)}
              onBlur={() => handleBlur("village")}
              className={inputClass(touched.village, errors.village)}
            />
          </InputField>

          <InputField
            label="District"
            id="district"
            required
            error={errors.district}
            touched={touched.district}
          >
            <input
              type="text"
              placeholder="Enter district"
              maxLength={60}
              id="district"
              value={form.district}
              onChange={(e) => handleChange("district", e.target.value)}
              onBlur={() => handleBlur("district")}
              className={inputClass(touched.district, errors.district)}
            />
          </InputField>

          <InputField
            label="State"
            id="state"
            required
            error={errors.state}
            touched={touched.state}
          >
            <select
              name="state"
              id="state"
              value={form.state}
              onChange={(e) => handleChange("state", e.target.value)}
              onBlur={() => handleBlur("state")}
              className={inputClass(touched.state, errors.state)}
            >
              <option value="">Select State</option>
              {STATES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </InputField>
        </div>

        <SectionLabel>Bank Details</SectionLabel>
        <div className="form-grid form-grid-2">
          <InputField
            label="Account Number"
            id="bankAccount"
            required
            error={errors.bankAccount}
            touched={touched.bankAccount}
          >
            <input
              id="bankAccount"
              type="text"
              maxLength={18}
              placeholder="9–18 digit account number"
              value={form.bankAccount}
              onChange={(e) =>
                handleChange("bankAccount", e.target.value.replace(/\D/g, ""))
              }
              onBlur={() => handleBlur("bankAccount")}
              className={inputClass(touched.bankAccount, errors.bankAccount)}
            />
          </InputField>

          <InputField
            label="IFSC Code"
            id="ifscCode"
            required
            hint="4 letters + 0 + 6 alphanumeric"
            error={errors.ifscCode}
            touched={touched.ifscCode}
          >
            <input
              id="ifscCode"
              type="text"
              maxLength={11}
              placeholder="e.g. SBIN0001234"
              value={form.ifscCode}
              onChange={(e) =>
                handleChange(
                  "ifscCode",
                  e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, ""),
                )
              }
              onBlur={() => handleBlur("ifscCode")}
              className={inputClass(touched.ifscCode, errors.ifscCode)}
            />
          </InputField>
        </div>

        <SectionLabel>Status</SectionLabel>
        <div className="status-group">
          {["ACTIVE", "INACTIVE"].map((s) => (
            <label key={s} className="status-option">
              <input
                type="radio"
                name="status"
                value={s}
                checked={form.status === s}
                onChange={() => handleChange("status", s)}
              />
              <StatusBadge status={s} />
            </label>
          ))}
        </div>

        {submitError && <p className="form-submit-error">{submitError}</p>}

        <div className="form-actions">
          <button type="button" onClick={handleCancel} className="btn btn-secondary">
            Cancel
          </button>
          <button type="submit" disabled={loading} className="btn btn-primary">
            {loading ? (
              <>
                <svg className="spinner" fill="none" viewBox="0 0 24 24">
                  <circle
                    className="spinner-track"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                  />
                  <path
                    className="spinner-head"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
                  />
                </svg>
                {isEdit ? "Saving..." : "Registering..."}
              </>
            ) : (
              <>
                <svg className="icon-check" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={1.5}
                    d="M5 13l4 4L19 7"
                  />
                </svg>
                {isEdit ? "Save changes" : "Register farmer"}
              </>
            )}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateFarmer;