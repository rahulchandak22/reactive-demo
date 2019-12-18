package com.reactive.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public
class Movie {

    @Id
    private String id;
    private String title;

}
