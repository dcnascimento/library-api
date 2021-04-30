package com.camelodev.libraryapi.service;

import com.camelodev.libraryapi.model.entity.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

    Book save(Book any);
}
