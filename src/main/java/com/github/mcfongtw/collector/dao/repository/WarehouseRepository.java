package com.github.mcfongtw.collector.dao.repository;

import com.github.mcfongtw.collector.dao.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="warehouses")
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
}
