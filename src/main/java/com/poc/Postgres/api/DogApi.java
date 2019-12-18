package com.poc.Postgres.api;

import com.poc.Postgres.api.dto.DogInputDto;
import com.poc.Postgres.api.dto.DogOutputDto;
import com.poc.Postgres.model.Dog;
import com.poc.Postgres.service.DogService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class DogApi {

    private final DogService dogService;
    private final ModelMapper modelMapper;

    public DogApi(DogService dogService) {
        this.dogService = dogService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping("/dogs")
    public ResponseEntity<List<DogOutputDto>> list(@RequestParam(defaultValue = "") String name) {
        List<DogOutputDto> dogsOutputDto = dogService.list(name)
                .stream()
                .map(dog -> modelMapper.map(dog, DogOutputDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dogsOutputDto);
    }

    @GetMapping("/dogs/{id}")
    public ResponseEntity<DogOutputDto> update(@PathVariable Long id) {
        return dogService.consult(id)
                .map(dog -> modelMapper.map(dog,DogOutputDto.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/dogs")
    public ResponseEntity<DogOutputDto> create(@RequestBody DogInputDto dogInputDto) {
        return Stream.of(modelMapper.map(dogInputDto, Dog.class))
                .map(dogService::create)
                .map(dog -> modelMapper.map(dog, DogOutputDto.class))
                .map(dogOutputDto -> ResponseEntity.status(HttpStatus.CREATED).body(dogOutputDto))
                .findFirst()
                .get();
    }

    @PutMapping("/dogs/{id}")
    public ResponseEntity<DogOutputDto> update(@PathVariable Long id, @RequestBody DogInputDto dogInputDto) {
        return Stream.of(modelMapper.map(dogInputDto, Dog.class))
                .map(dog -> dogService.update(dog, id))
                .map(dog -> modelMapper.map(dog, DogOutputDto.class))
                .map(ResponseEntity::ok)
                .findFirst()
                .get();
    }

    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Dog> dog = dogService.consult(id);
        if(dog.isPresent()) {
            dogService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }
}
