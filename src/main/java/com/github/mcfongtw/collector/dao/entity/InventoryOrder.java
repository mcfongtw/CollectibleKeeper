package com.github.mcfongtw.collector.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "inventory_order")
@Entity(name = "InventoryOrder")
public class InventoryOrder extends AbstractEntity {

    @EqualsAndHashCode.Exclude
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency = DEFAULT_CURRENCY_TYPE;

    public static final Currency DEFAULT_CURRENCY_TYPE = Currency.TWD;

    public enum Currency {
        USD,
        TWD
    }

    //TODO: performance improvement
    //Bidirectional OneToOne
    @OneToOne(
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonIgnore
    private Inventory inventory;


    @Column(name = "ordered_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderedType = ORDER_TYPE_BUY;

    public static final OrderType ORDER_TYPE_BUY = OrderType.BUY;

    public static final OrderType ORDER_TYPE_SELL = OrderType.SELL;

    public enum OrderType {
        BUY,
        SELL
    }

    @Column(name = "ordered_price", nullable = false)
    private Double orderedPrice;

    @Column(name = "ordered_date", nullable = false)
    private Date orderedDate;

}

