package com.github.mcfongtw.collector.dao.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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

    //XXX: Returning a List for the sake of Theymeleaf Compatibile
    // since a Set has no indexes.
    public List<Inventory> getInventoriesAsList() {
        return Lists.newArrayList(inventories);
    }

    public void addInventory(Inventory inventory) {
        this.getInventories().add(inventory);
        inventory.setWarehouse(this);
    }

    public void removeInventory(Inventory inventory) {
        this.getInventories().remove(inventory);
        inventory.setWarehouse(null);
    }
}
