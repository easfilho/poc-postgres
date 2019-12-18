package com.poc.Postgres.service;

import com.poc.Postgres.model.Dog;
import com.poc.Postgres.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public List<Dog> list(String name) {
        return dogRepository.findAllByNameContainingIgnoringCase(name);
    }

    public Dog create(Dog dog) {
        return dogRepository.save(dog);
    }

    public Dog update(Dog dog, Long id) {
        Dog dogToUpdate = consult(id)
                .orElseThrow(() -> new NoSuchElementException("Dog not find for id " + id));

        dogToUpdate.setName(dog.getName());
        dogToUpdate.setAge(dog.getAge());
        dogToUpdate.setSize(dog.getSize());
        dogToUpdate.setRace(dog.getRace());
        return dogRepository.save(dogToUpdate);
    }

    public Optional<Dog> consult(Long id) {
        return dogRepository.findById(id);
    }

    public void delete(Long id) {
        dogRepository.deleteById(id);
    }
}
