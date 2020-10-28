package com.example.demo.repositories;

import com.example.demo.entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository <Book, String> {

    //Optional<Book> findBookByName(String name);
}
