package com.example.demo.controller;

import com.example.demo.dto.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final List<Person> persons = new ArrayList<>();
    private final AtomicInteger nextPersonId = new AtomicInteger(1);
    private Person person;

    @GetMapping("/person")
    public Person getPerson() {
        return new Person(1, "Ivanov", "Ivan", "Ivanovich", LocalDate.of(1999, 06, 07));
    } ; 

    @GetMapping
    public List<Person> getAllPersons() {
        return persons;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable int id) {
        return persons.stream()
                .filter(person -> person.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        person.setId(nextPersonId.getAndIncrement());
        persons.add(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person updatedPerson) {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getId() == id) {
                updatedPerson.setId(id);
                persons.set(i, updatedPerson);
                return ResponseEntity.ok(updatedPerson);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
       if (persons.removeIf(person -> person.getId() == id)) {
           return ResponseEntity.noContent().build();
       }
       return ResponseEntity.notFound().build();
    }
}
