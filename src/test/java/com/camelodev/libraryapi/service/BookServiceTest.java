package com.camelodev.libraryapi.service;

import com.camelodev.libraryapi.model.entity.Book;
import com.camelodev.libraryapi.model.repository.BookRepository;
import com.camelodev.libraryapi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        // cenário
        Book book = Book.builder().isbn("123").author("Fulano").title("Poemas e Poesias").build();
        Mockito.when(repository.save(book))
                .thenReturn(Book.builder()
                                .id(1L)
                                .isbn("123")
                                .author("Fulano")
                                .title("Poemas e Poesias")
                                .build());

        // execução
        Book savedBook = service.save(book);

        // verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("Poemas e Poesias");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");

    }

}