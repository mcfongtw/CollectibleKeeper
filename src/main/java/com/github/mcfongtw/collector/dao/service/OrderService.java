package com.github.mcfongtw.collector.dao.service;

import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.repository.InventoryOrderRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private InventoryOrderRepository inventoryOrderRepository;

    public List<InventoryOrder> getListOfOrderAbovePurchasedPrice(Double purchasedPrice) {
        return this.getListOfOrderAboveOrderedPrice(purchasedPrice, InventoryOrder.ORDER_TYPE_BUY);
    }

    public List<InventoryOrder> getListOfOrderAboveSoldPrice(Double soldPrice) {
        return this.getListOfOrderAboveOrderedPrice(soldPrice, InventoryOrder.ORDER_TYPE_SELL);
    }

    private List<InventoryOrder> getListOfOrderAboveOrderedPrice(Double orderedPrice, InventoryOrder.OrderType orderedType) {
        List<InventoryOrder> result =Lists.newArrayList();

        for(InventoryOrder inventoryOrder: inventoryOrderRepository.findByOrderedType(orderedType)) {
            if(inventoryOrder.getOrderedPrice() > orderedPrice) {
                result.add(inventoryOrder);
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<InventoryOrder> getListOfOrderBelowPurchasedPrice(Double purchasedPrice) {
        return this.getListOfOrderBelowOrderedPrice(purchasedPrice, InventoryOrder.ORDER_TYPE_BUY);
    }

    public List<InventoryOrder> getListOfOrderBelowSoldPrice(Double soldPrice) {
        return this.getListOfOrderBelowOrderedPrice(soldPrice, InventoryOrder.ORDER_TYPE_SELL);
    }

    private List<InventoryOrder> getListOfOrderBelowOrderedPrice(Double givenOrderedPrice, InventoryOrder.OrderType orderedType) {
        List<InventoryOrder> result =Lists.newArrayList();

        for(InventoryOrder inventoryOrder: inventoryOrderRepository.findByOrderedType(orderedType)) {
            if(inventoryOrder.getOrderedPrice() < givenOrderedPrice) {
                result.add(inventoryOrder);
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public List<InventoryOrder> getListOfOrderBeforePurchasedDate(Date purchaseDate) {
        return this.getListOfOrderBeforeOrderedDate(purchaseDate, InventoryOrder.ORDER_TYPE_BUY);
    }

    public List<InventoryOrder> getListOfOrderBeforeSoldDate(Date soldDate) {
        return this.getListOfOrderBeforeOrderedDate(soldDate, InventoryOrder.ORDER_TYPE_SELL);
    }

    private List<InventoryOrder> getListOfOrderBeforeOrderedDate(Date givenOrderedDate, InventoryOrder.OrderType orderedType) {
        List<InventoryOrder> result =Lists.newArrayList();

        for(InventoryOrder inventoryOrder: inventoryOrderRepository.findByOrderedType(orderedType)) {
            if(inventoryOrder.getOrderedDate().before(givenOrderedDate)) {
                result.add(inventoryOrder);
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<InventoryOrder> getListOfOrderAfterPurchasedDate(Date purchaseDate) {
        return this.getListOfOrderAfterOrderedDate(purchaseDate, InventoryOrder.ORDER_TYPE_BUY);
    }

    public List<InventoryOrder> getListOfOrderAfterSoldDate(Date soldDate) {
        return this.getListOfOrderAfterOrderedDate(soldDate, InventoryOrder.ORDER_TYPE_SELL);
    }

    private List<InventoryOrder> getListOfOrderAfterOrderedDate(Date givenOrderedDate, InventoryOrder.OrderType orderedType) {
        List<InventoryOrder> result =Lists.newArrayList();

        for(InventoryOrder inventoryOrder: inventoryOrderRepository.findByOrderedType(orderedType)) {
            if(inventoryOrder.getOrderedDate().after(givenOrderedDate)) {
                result.add(inventoryOrder);
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public int getAggregatedNumberOfInventoryFromOrder(String sku) {
        int result = 0;

        result += inventoryOrderRepository.findByInventorySku(sku).size();

        return result;
    }
}
