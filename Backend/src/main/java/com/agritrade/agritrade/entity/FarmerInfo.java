package com.agritrade.agritrade.entity;

import com.agritrade.agritrade.Enum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="farmer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long farmerId;
    private String farmerName;
    private String phone;
    private String village;
    private String district;
    private String state;
    private String aadhaarNumber;
    private String bankAccount;
    private String ifscCode;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }


}
