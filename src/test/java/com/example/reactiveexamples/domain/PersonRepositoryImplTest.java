package com.example.reactiveexamples.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.SQLOutput;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jpjensen
 * @version %I%
 * @since
 */
class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = this.personRepository.getById(1);
        Person person = personMono.block();

        System.out.println(person.toString());
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = this.personRepository.getById(1);
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = this.personRepository.getById(1);

        personMono.map(person -> {
            System.out.println(person.toString());
            return person.getFirstName();
        }).subscribe(firstname -> System.out.println("from map: " + firstname));

    }

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();

        System.out.println(person.toString());

    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFluxToListMono() {
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> personListMono = personFlux.collectList();

        personListMono.subscribe(list -> list.forEach(person -> System.out.println(person.toString())));
    }

    @Test
    void testFindPersonById() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 3;
        Mono<Person> personMono = personFlux.filter(person -> id.equals(person.getId())).next();

        personMono.subscribe(person -> System.out.println(person.toString()));

    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> id.equals(person.getId())).next();

        personMono.subscribe(person -> System.out.println(person.toString()));

    }

    @Test
    void testFindPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;
        Mono<Person> personMono = personFlux.filter(person -> id.equals(person.getId())).single();

        personMono.doOnError(throwable -> System.out.println("I went boom")).onErrorReturn(Person.builder().id(id).build()).subscribe(person -> System.out.println(person.toString()));

    }

    @Test
    void testFindPersonByIdShouldBeMichael() {
        Mono<Person> personMono = personRepository.getById(1);
        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
        Person person = personMono.block();
        assertEquals("Michael", person.getFirstName());
    }

    @Test
    void testFindPersonByIdShouldNotFind() {
        Mono<Person> personMono = personRepository.getById(8);
        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();
        Person person = personMono.block();
        assertNull(person);
    }

}
