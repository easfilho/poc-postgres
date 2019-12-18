package com.poc.Postgres.repository;

import com.poc.Postgres.model.Dog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DogRepository extends CrudRepository<Dog, Long> {

    List<Dog> findAllByNameContainingIgnoringCase(String name);
}
