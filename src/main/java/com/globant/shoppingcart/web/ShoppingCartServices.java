package com.globant.shoppingcart.web;

import com.globant.shoppingcart.domain.ItemRequest;
import com.globant.shoppingcart.domain.ShoppingCart;
import com.globant.shoppingcart.usecase.ShoppingCartUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Esta clase define los servicios para administrar un carro de compras.
 * @author: Aidelis C Calvo.
 * @version: 15/08/2018/A
 */

@RestController
@RequestMapping("api_shoppingcart")
public class ShoppingCartServices {

    @Autowired
    private ShoppingCartUseCase useCase;

    /**
     * Crea un nuevo carro de compras.
     * @param request: Objeto con los atributos necesarios para la creación de un carro de compras.
     * @return la creación de un carro de compras.
     */
    @PostMapping(path = "create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ShoppingCart> create(@RequestBody ShoppingCart request) {
        return useCase.createNewShoppingCart(request);
    }


    /**
     * Añade un item de un artículo seleccionado al carro de compras.
     * @param itemRequest: Objeto que contiene el id del carrito, el artículo y la cantidad,
     *                    necesarios para la adición del item.
     * @return actualización del carro con el nuevo item.
     */
    @PutMapping(path = "addItem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ShoppingCart> addItemToCart(@RequestBody ItemRequest itemRequest) {
        return useCase.addItemToCart(itemRequest.getCartId(), itemRequest);
    }


    /**
     * Remueve un item del carro de compras.
     * @param itemRequest: Objeto con el id del carro y el item a eliminar .
     * @return actualización del carro con el item eliminado.
     */
    @PutMapping(path = "removeItem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ShoppingCart> removeItem(@RequestBody ItemRequest itemRequest) {
        return useCase.removeItem(itemRequest.getCartId(), itemRequest);
    }


    /**
     * Actualiza la cantidad de un item del carro de compras.
     * @param itemRequest: Objeto con el id del carro y el item a actualizar .
     * @return actualización del item de un carro con el valor de la nueva cantidad de un artículo.
     */
    @PutMapping(path = "updateQuantity", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ShoppingCart> updateQuantity(@RequestBody ItemRequest itemRequest) {
        return useCase.updateQuantity(itemRequest.getCartId(), itemRequest);
    }


    /**
     * Obtiene información de un carro de compras.
     * @param cartId: id del carro del que se quiere obtener información.
     * @return Devuelve información como nombre y lista de items agregados a un carro.
     */
    @GetMapping(path = "get/{cartId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ShoppingCart> getCart(@PathVariable String cartId) {
        return useCase.getInformationCart(cartId);
    }


    /**
     * Obtiene el monto total a pagar de acuerdo a la cantidad y precio de los artículos seleccionados.
     * @param cartId: id del carro del que se quiere obtener el monto total a pagar.
     * @return Devuelve el valor del monto total a pagar.
     */
    @GetMapping(path = "getTotalToPaid/{cartId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Double> getTotalToPaid(@PathVariable String cartId) {
        return useCase.getTotalToPaid(cartId);
    }
}