package com.github.mcfongtw.collector.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Table(name = "inventory")
@Entity(name = "Inventory")
@EqualsAndHashCode(callSuper = true)
public class Inventory extends AbstractCollectable {

    @EqualsAndHashCode.Exclude
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", nullable = false)
    private String id;

    //Bidirectional @OneToMany
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade={CascadeType.PERSIST,CascadeType.MERGE}
    )
    @JoinColumn(name = "warehouse_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Warehouse warehouse;


    //Bidirectional OneToOne
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinColumn(name = "inventory_order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private InventoryOrder inventoryOrder;
}
