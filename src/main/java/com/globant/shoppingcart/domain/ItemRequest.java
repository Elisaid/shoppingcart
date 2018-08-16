package com.globant.shoppingcart.domain;

import lombok.Data;

@Data
public class ItemRequest extends Item {

    private String cartId;
}
