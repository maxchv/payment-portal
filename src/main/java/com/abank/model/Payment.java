package com.abank.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Column(name = "payment_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_acc_id")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "dest_acc_id")
    private Account destinationAccount;

    @Positive
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
