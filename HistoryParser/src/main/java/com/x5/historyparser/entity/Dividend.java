package com.x5.historyparser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "dividend", schema = "bank")
public class Dividend {
    public static final String STATE_EXPECTED = "expected";
    public static final String STATE_PAID = "paid";
    public static final String STATE_FORECAST = "forecast";
    public static final String COMPANY_CODE_X5 = "x5";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dividend_id_gen")
    @SequenceGenerator(name = "dividend_id_gen", sequenceName = "bank.dividend__seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "state", nullable = false, length = 32)
    private String state;

    @Column(name = "state_date", nullable = false)
    private LocalDateTime stateDate;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_code", nullable = false, length = 32)
    private String companyCode;

    @Column(name = "dividend_declaration_date")
    private LocalDate dividendDeclarationDate;

    @Column(name = "close_register_date", nullable = false)
    private LocalDate closeRegisterDate;

    @Column(name = "dividend_period", length = 64)
    private String dividendPeriod;

    @Column(name = "dividend_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal dividendAmount;

    @Column(name = "buy_before_date")
    private LocalDate buyBeforeDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

}