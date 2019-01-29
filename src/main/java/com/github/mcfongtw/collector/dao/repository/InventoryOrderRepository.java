package com.github.mcfongtw.collector.dao.repository;

import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="orders")
public interface InventoryOrderRepository extends JpaRepository<InventoryOrder, String> {

    /**
     *
     * @param orderedType
     * @return
     */
    List<InventoryOrder> findByOrderedType(InventoryOrder.OrderType orderedType);

    /**
     *
     * @param sku
     * @return
     */
    @Query(
    "SELECT inv_order FROM InventoryOrder inv_order INNER JOIN inv_order.inventory inv WHERE inv.sku = ?1"
    )
    List<InventoryOrder> findByInventorySku(String sku);
}
