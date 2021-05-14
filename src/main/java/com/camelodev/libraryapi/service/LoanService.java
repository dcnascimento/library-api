package com.camelodev.libraryapi.service;

import com.camelodev.libraryapi.api.resource.BookController;
import com.camelodev.libraryapi.model.entity.Loan;

import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);
}
