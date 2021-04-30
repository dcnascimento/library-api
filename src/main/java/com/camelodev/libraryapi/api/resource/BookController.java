package com.camelodev.libraryapi.api.resource;

import com.camelodev.libraryapi.api.dto.BookDTO;
import com.camelodev.libraryapi.model.entity.Book;
import com.camelodev.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public BookDTO create(@RequestBody BookDTO dto){
        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }
}
