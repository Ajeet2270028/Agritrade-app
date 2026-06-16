package com.agritrade.agritrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="purchase_grain")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseGrain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;
    private Enu
}
