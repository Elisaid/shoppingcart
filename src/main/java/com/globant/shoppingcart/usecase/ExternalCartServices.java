package com.globant.shoppingcart.usecase;

import com.globant.shoppingcart.domain.Article;
import com.globant.shoppingcart.domain.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ExternalCartServices {

    @Value("${external.urls.cart}")
    private String communicationShoppingCartUrl;


    public Mono<Article> getArticleById(String articleId){
        WebClient client = WebClient.create(communicationShoppingCartUrl);
        return client.get()
                .uri("/articles/{id}", articleId)
                .header("Content-Type", "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(Article.class))
                .onErrorMap(i -> new BusinessException("El artículo no se encuentra disponible."));
    }
}
