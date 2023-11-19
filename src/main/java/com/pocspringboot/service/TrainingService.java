package com.pocspringboot.service;

import com.pocspringboot.model.request.training.Product;
import com.pocspringboot.model.request.training.TrainingNumberReverseRequest;
import com.pocspringboot.model.request.training.ProductListRequest;
import com.pocspringboot.model.response.training.ProductListResponse;
import com.pocspringboot.model.response.training.SumEvenOddResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {

    public Integer reverseString(TrainingNumberReverseRequest request) {
        int signal = (int) Math.signum(request.getNumber());
        String reversedString = new StringBuilder(String.valueOf(Math.abs(request.getNumber()))).reverse().toString();
        return Integer.parseInt(reversedString) * signal;
    }

    public Double calculateProductsPriceSum(ProductListRequest request) {
        return request.getProducts().stream()
                .map(Product::getPrice)
                .reduce(0.0, (accumulator, combiner) -> {
                    log.info(String.format("Accumulator: %s Combiner: %s", accumulator, combiner));
                    return accumulator + combiner;
                });
    }

    public Double calculateProductPriceAverage(ProductListRequest request) {
        return request.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0);
    }

    public List<String> convertUppercaseToLowercase(ProductListRequest request) {
        return request.getProducts().stream()
                .map(Product::getName)
                .map(String::toLowerCase)
                .toList();
    }

    public SumEvenOddResponse calculateSumEvenOdd(ProductListRequest request) {
        Long evenSum = request.getProducts().stream()
                .map(Product::getId)
                .reduce(0L, (accumulator, id) -> {
                    boolean isEven = id % 2 == 0;
                    return isEven ? accumulator + 1 : accumulator;
                });

        return SumEvenOddResponse.builder()
                .even(evenSum)
                .odd(request.getProducts().size() - evenSum)
                .build();
    }

    public ProductListResponse removeDuplicatesIdFromRequest(ProductListRequest request) {
        Set<Long> uniqueIds = request.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
        log.info(String.format("uniqueIds: %s", uniqueIds));

        List<Product> productList = request.getProducts().stream()
                .filter(product -> {
                    boolean isInSet = uniqueIds.contains(product.getId());
                    if (isInSet) uniqueIds.remove(product.getId());
                    return isInSet;
                })
                .toList();

        return ProductListResponse.builder()
                .products(productList)
                .build();
    }

    public Long countLetter(ProductListRequest request, String letter) {
        return request.getProducts().stream()
                .map(Product::getName)
                .filter(name -> name.startsWith(letter))
                .count();
    }

    public ProductListResponse sortAlphabetical(ProductListRequest request) {
        ProductListResponse response = new ProductListResponse();
        request.getProducts().stream()
                .sorted(Comparator.comparing(o -> o.getName().toLowerCase()))
                .forEach(p -> response.getProducts().add(p));
        return response;
    }

}
