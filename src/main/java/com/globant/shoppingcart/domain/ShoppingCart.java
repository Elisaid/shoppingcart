package com.globant.shoppingcart.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class ShoppingCart {
    @Id
    private String id;
    private String name;
    private List<Item> items;

}
