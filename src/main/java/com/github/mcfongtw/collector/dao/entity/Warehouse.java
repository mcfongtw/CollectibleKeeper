package com.github.mcfongtw.collector.dao.entity;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Data
@Table(name = "warehouse")
@Entity(name = "Warehouse")
public class Warehouse extends AbstractEntity {

    @EqualsAndHashCode.Exclude
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", nullable = false)
    private String id;


    @Column(name = "name", nullable = false)
    private String name;

    //Bidirectional @OneToMany
    @OneToMany(
            mappedBy = "warehouse",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Inventory> inventories = Sets.newHashSet();

    public void addInventory(Inventory inventory) {
        this.getInventories().add(inventory);
        inventory.setWarehouse(this);
    }

    public void removeInventory(Inventory inventory) {
        this.getInventories().remove(inventory);
        inventory.setWarehouse(null);
    }
}