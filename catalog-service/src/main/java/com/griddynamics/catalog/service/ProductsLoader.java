package com.griddynamics.catalog.service;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.griddynamics.catalog.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductsLoader {
    @Value("${catalog-path :classpath:csv/jcpenney_com-ecommerce_sample.csv}")
    private Resource productsFile;
    private final ProductsService data;

    @PostConstruct
    public void init() throws IOException {
        List<Product> allProducts = loadAllProducts(productsFile.getFile());
        log.info("Loaded {} products", allProducts.size());
        data.update(allProducts);
    }

    private List<Product> loadAllProducts(File file) throws IOException {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        try (MappingIterator<Product> readValues = mapper.readerFor(Product.class).with(schema).readValues(file)) {
            return readValues.readAll();
        }
    }
}
