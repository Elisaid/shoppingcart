package com.globant.shoppingcart.usecase;


import com.globant.shoppingcart.domain.Article;
import com.globant.shoppingcart.domain.ItemRequest;
import com.globant.shoppingcart.domain.ShoppingCart;
import com.globant.shoppingcart.domain.repository.ShoppingCartRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartUseCaseTest {

    @InjectMocks
    private ShoppingCartUseCase shoppingCartUseCase;

    @Mock
    private ShoppingCartRepository repository;

    @Mock
    private Mono<ShoppingCart> shoppingCartMono;

    @Mock
    private ExternalCartServices externalCartServices;

    @Mock
    private Mono<Article> articleMono;

    private ItemRequest itemRequest;

    private ShoppingCart shoppingCart;

    @Before
    public void setUp(){
        shoppingCart = new ShoppingCart();
        shoppingCart.setName("My Shopping Car");
        shoppingCart.setItems(new ArrayList<>());

        itemRequest = new ItemRequest();
        itemRequest.setArticleId("1");
        itemRequest.setCartId("id0001");
        itemRequest.setQuantity(2);
    }
    @Test
    public void shouldCreateANewShoppingCart() {

        Mockito.when(repository.save(shoppingCart)).thenReturn(shoppingCartMono);

        shoppingCartUseCase.createNewShoppingCart(shoppingCart);

        verify(repository).save(shoppingCart);

    }

    @Test
    public void shouldGetInformationOfACart() {

        when(repository.findById(Mockito.anyString())).thenReturn(shoppingCartMono);

        shoppingCartUseCase.getInformationCart(Mockito.anyString());

        assertThat(shoppingCartMono).isNotNull();
    }

    @Test
    public void shouldAddItemToCart() {

        Mockito.when(externalCartServices.getArticleById(Mockito.anyString())).thenReturn(articleMono);

        shoppingCartUseCase.addItemToCart(itemRequest.getCartId(), itemRequest);

        verify(externalCartServices).getArticleById(Mockito.anyString());
        assertThat(itemRequest).isNotNull();

    }

    @Test
    public void shouldRemoveItemToCart() {
        Mockito.when(repository.findById(Mockito.anyString())).thenReturn(shoppingCartMono);

        Mono<ShoppingCart> shoppingCartMono = shoppingCartUseCase.removeItem(itemRequest.getCartId(), itemRequest);

        verify(repository).findById(Mockito.anyString());
        assertThat(shoppingCartMono).isNotNull();
        assertThat(itemRequest).isNotNull();
    }

}
