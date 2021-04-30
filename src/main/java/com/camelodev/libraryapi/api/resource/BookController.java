package com.camelodev.libraryapi.api.resource;

import com.camelodev.libraryapi.api.dto.BookDTO;
import com.camelodev.libraryapi.model.entity.Book;
import com.camelodev.libraryapi.service.BookService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public BookDTO create(@RequestBody BookDTO dto){
        Book entity = Book.builder()
                .author(dto.getAuthor())
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .build();
        entity = service.save(entity);

        return BookDTO.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .isbn(entity.getIsbn())
                .build();
    }
}
