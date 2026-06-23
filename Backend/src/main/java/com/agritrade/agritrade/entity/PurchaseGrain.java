package com.agritrade.agritrade.entity;

import com.agritrade.agritrade.Enum.GrainType;
import com.agritrade.agritrade.Enum.Status;
import com.agritrade.agritrade.entity.FarmerInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_grain")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseGrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    // ── Farmer reference (replaces duplicate farmerName / phone / village) ──
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private FarmerInfo farmer;

    // ── Grain details ──
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GrainType grainType;

    @Column(nullable = false)
    private Double grainRatePerKg;

    // ── Each bag weight stored individually ──
    // Creates a separate table: purchase_grain_bag_weights(purchase_id, bag_weight)
    @ElementCollection
    @CollectionTable(
            name = "purchase_grain_bag_weights",
            joinColumns = @JoinColumn(name = "purchase_id")
    )
    @Column(name = "bag_weight")
    private List<Double> bagWeights = new ArrayList<>();

    // ── Computed / tracked totals (updated on every bag addition) ──
    private Double totalGrainWeight = 0.0;      // sum of all bagWeights
    private Long   totalBags        = 0L;        // bagWeights.size()
    private Double totalPrice       = 0.0;       // totalGrainWeight * grainRatePerKg

    private Double totalSoldWeight      = 0.0;   // updated when grain is sold
    private Double totalRemainGrainWeight = 0.0; // totalGrainWeight - totalSoldWeight

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Helper: call this every time a bag weight is added ──
    public void addBagWeight(Double weight) {
        this.bagWeights.add(weight);
        this.totalBags          = (long) this.bagWeights.size();
        this.totalGrainWeight   = this.bagWeights.stream().mapToDouble(Double::doubleValue).sum();
        this.totalRemainGrainWeight = this.totalGrainWeight - this.totalSoldWeight;
        if (this.grainRatePerKg != null) {
            this.totalPrice = this.totalGrainWeight * this.grainRatePerKg;
        }
    }

    // ── Helper: call this when grain is sold/dispatched ──
    public void updateSoldWeight(Double soldWeight) {
        this.totalSoldWeight        = soldWeight;
        this.totalRemainGrainWeight = this.totalGrainWeight - this.totalSoldWeight;
    }
}