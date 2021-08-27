package com.example.reactiveexamples.domain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author jpjensen
 * @version %I%
 * @since
 */
@Slf4j
public class PersonRepositoryImpl implements PersonRepository {

    Person michael = new Person(1, "Michael", "Weston");
    Person fiona = new Person(2, "Fiona", "Glenanne");
    Person sam = new Person(3, "Sam", "Axe");
    Person jesse = new Person(4, "Jesse", "Porter");

    @Override
    public Mono<Person> getById(Integer id) {
        return findAll().filter(person -> id.equals(person.getId())).single()
                .doOnError(throwable -> log.error("Failed to find user with id: {}", id, throwable))
                .onErrorReturn(Person.builder().build());
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(michael, fiona, sam, jesse);
    }
}
