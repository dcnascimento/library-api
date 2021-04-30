package com.camelodev.libraryapi.api.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;

}
