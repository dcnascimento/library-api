package com.camelodev.libraryapi.api.resource;

import com.camelodev.libraryapi.api.dto.BookDTO;
import com.camelodev.libraryapi.api.exception.ApiErrors;
import com.camelodev.libraryapi.exception.BusinessException;
import com.camelodev.libraryapi.model.entity.Book;
import com.camelodev.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

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
    public BookDTO create(@RequestBody @Valid BookDTO dto){
        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("/{id}")
    public BookDTO get(@PathVariable Long id){
        return service.getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id){
        Book book =  service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        service.delete(book);
    }

    @PutMapping("/{id}")
    public BookDTO update(@PathVariable Long id, BookDTO dto){
        Book book =  service.getById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        book = service.update(book);
        return modelMapper.map(book, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex){
        return new ApiErrors(ex);
    }
}
