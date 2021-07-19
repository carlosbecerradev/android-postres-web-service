package com.example.androidwebservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.androidwebservice.model.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

}
