package com.globant.shoppingcart.usecase;

import com.globant.shoppingcart.domain.Article;
import com.globant.shoppingcart.domain.Item;
import com.globant.shoppingcart.domain.ItemRequest;
import com.globant.shoppingcart.domain.ShoppingCart;
import com.globant.shoppingcart.domain.exceptions.BusinessException;
import com.globant.shoppingcart.domain.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static reactor.core.publisher.Mono.just;

@Component
public class ShoppingCartUseCase {

    @Autowired
    private ShoppingCartRepository repository;

    @Autowired
    private ExternalCartServices externalCartServices;

    public Mono<ShoppingCart> createNewShoppingCart(ShoppingCart request){
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setItems(new ArrayList<>());
        shoppingCart.setName(request.getName());
        return repository.save(shoppingCart);
    }

    public Mono<ShoppingCart> addItemToCart(String cartId, Item itemRequest) {
        Mono<Article> articleById = externalCartServices.getArticleById(itemRequest.getArticleId());
       return articleById.flatMap( i -> addItem(cartId, itemRequest));
    }

    private Mono<ShoppingCart> addItem(String cartId, Item itemRequest) {
        Mono<ShoppingCart> shoppingCart = repository.findById(cartId);
        return shoppingCart.flatMap(cart -> updateCart(cart, itemRequest));
    }

    private Mono<ShoppingCart> updateCart(ShoppingCart cart, Item itemRequest) {
        Optional<Item> optionalItem = cart.getItems().stream().filter(item -> item.getArticleId().equals(itemRequest.getArticleId())).findFirst();
        if(optionalItem.isPresent()){
            throw new BusinessException("Este artículo ya fue agregado al carro de compras!");
        }
        return just(cart.getItems()).flatMap(listItems -> {
            Item item = new Item();
            item.setArticleId(itemRequest.getArticleId());
            item.setQuantity(itemRequest.getQuantity());
            listItems.add(item);
            cart.setItems(listItems);
            return repository.save(cart);
        });
    }

    public Mono<ShoppingCart> getInformationCart(String cartId) {
        return  repository.findById(cartId);
    }

    public Mono <Double> getTotalToPaid(String cartId) {
        Mono<ShoppingCart> cart = repository.findById(cartId);
        Mono<List<Item>> itemsByCart = cart.map(c -> c.getItems());
        Flux<Double> priceArticles = itemsByCart.flatMapMany(items -> Flux.fromIterable(items)
                .flatMap(i -> getPriceByQuantity(i.getArticleId(), i.getQuantity())));

        return priceArticles.collectList().map(i -> i.stream().mapToDouble(price -> Double.valueOf(price)).sum());
    }

    private Mono<Double> getPriceByQuantity(String articleID, Integer quantity){
        Mono<Article> articleById = externalCartServices.getArticleById(articleID);
        return articleById.map(article -> new BigDecimal(Double.parseDouble(article.getPrice())* quantity).setScale(2, RoundingMode.DOWN).doubleValue());
    }

    public Mono<ShoppingCart> removeItem(String cartId, ItemRequest itemRequest) {
       return repository.findById(cartId)
                .flatMap(cart -> {
                    cart.getItems().removeIf(item -> item.getArticleId().equals(itemRequest.getArticleId()));
                return repository.save(cart);
                });
    }

    public Mono<ShoppingCart> updateQuantity(String cartId, ItemRequest itemRequest) {
        Mono<ShoppingCart> cart = repository.findById(cartId);
       return cart.flatMap(c -> {
           Item item = c.getItems().stream().filter(i -> i.getArticleId().equals(itemRequest.getArticleId())).findFirst().get();
           item.setQuantity(itemRequest.getQuantity());
           return Mono.just(c);
       }).flatMap(repository::save);
    }
}