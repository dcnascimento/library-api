package com.camelodev.libraryapi.service;

import com.camelodev.libraryapi.model.entity.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BookService {

    Book save(Book any);

    Optional<Book> getById(Long id);

    void delete(Book book);

    Book update(Book book);
}
