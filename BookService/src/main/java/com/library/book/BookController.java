package com.library.book;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/books")
public class BookController {

    @Value("${server.port}")
    private String port;

    @GetMapping
    public String getBooks() {

        return "Books fetched successfully from instance: "
                + port;
    }

    @PostMapping
    public String addBook(@RequestBody String book) {

        return "Book added successfully: " + book
                + " from instance: " + port;
    }
}