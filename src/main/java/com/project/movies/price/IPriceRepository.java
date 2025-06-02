package com.project.movies.price;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPriceRepository extends JpaRepository<PriceModel, Long> {
}
