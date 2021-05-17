package com.camelodev.libraryapi.service;

import com.camelodev.libraryapi.api.dto.LoanFilterDTO;
import com.camelodev.libraryapi.api.resource.BookController;
import com.camelodev.libraryapi.model.entity.Book;
import com.camelodev.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);

    List<Loan> getAllLoans();
}
