package com.github.mcfongtw.collector.dao.repository;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="inventories")
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    /**
     *
     * @param warehouseId
     * @return
     */
    List<Inventory> findByWarehouseId(String warehouseId);

    /**
     *
     * @param name
     * @return
     */
    @Query(
            "SELECT inv FROM Inventory inv INNER JOIN inv.warehouse house WHERE house.name = ?1"
    )
    List<Inventory> findByWarehouseName(String name);
}
