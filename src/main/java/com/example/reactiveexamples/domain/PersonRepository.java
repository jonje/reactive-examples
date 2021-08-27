package com.example.reactiveexamples.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author jpjensen
 * @version %I%
 * @since
 */
public interface PersonRepository {
    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
