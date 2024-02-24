package guru.springframework.apifirst.apifirstserver.controllers;


import guru.springframework.apifirst.apifirstserver.config.OpenApiValidationConfig;
import guru.springframework.apifirst.apifirstserver.domain.Product;
import guru.springframework.apifirst.model.DimensionsDto;
import guru.springframework.apifirst.model.ImageDto;
import guru.springframework.apifirst.model.ProductCreateDto;
import guru.springframework.apifirst.model.ProductUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(OpenApiValidationConfig.class)
class ProductControllerTest extends BaseTest {

    @Transactional
    @Test
    void testUpdateProduct() throws Exception {

        Product product = productRepository.findAll().iterator().next();

        ProductUpdateDto productUpdateDto = productMapper.productToUpdateDto(product);

        productUpdateDto.setDescription("Updated Description");

        mockMvc.perform(put(ProductController.BASE_URL + "/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", equalTo("Updated Description")));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductCreateDto newProduct = ProductCreateDto.builder()
                .description("New Product")
                .cost("5.00")
                .price("8.95")
                .categories(Arrays.asList("ELECTRONICS"))
                .images(Arrays.asList(ImageDto.builder()
                        .url("http://example.com/image.jpg")
                        .altText("Image Alt Text")
                        .build()))
                .dimensions(DimensionsDto.builder()
                        .length(10)
                        .width(10)
                        .height(10)
                        .build())
                .build();

        mockMvc.perform(post(ProductController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void listProducts() throws Exception {
        mockMvc.perform(get(ProductController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void getProductById() throws Exception {
        mockMvc.perform(get(ProductController.BASE_URL + "/{productId}", testProduct.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(testProduct.getId().toString())));
    }
}