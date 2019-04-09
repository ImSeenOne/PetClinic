package org.springframework.samples.petclinic.products;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.product.Product;
import org.springframework.samples.petclinic.product.ProductController;
import org.springframework.samples.petclinic.product.ProductRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    private static final int TEST_PRODUCT_ID = 1;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    private Product product;

    @Before
    public void setup() {
        product = new Product();

        product.setId(TEST_PRODUCT_ID);
        product.setName("Peluchito");
        product.setPrice("500");
        product.setStock("40");
        product.setImagePath("peluchito.jpg");
        product.setDescription("pequeñito jeje");
        given(this.productRepository.findById(TEST_PRODUCT_ID)).willReturn(product);
    }

    @WithMockUser(value="user")
    @Test
    public void testInitCreationProductForm() throws Exception{
        mockMvc.perform(get("/products/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("product"))
            .andExpect(view().name("appointments/createOrUpdateAppointmentForm"));
    }

    @WithMockUser(value = "user")
    @Test
    public void initEditFormTest() throws Exception {
        mockMvc.perform(get(
            "/products/{productId}/edit",TEST_PRODUCT_ID))

            .andExpect(model().attributeExists("product"))
            .andExpect(model().attribute("product", hasProperty("name", is("Peluchito"))))
            .andExpect(model().attribute("product", hasProperty("price", is("500"))))
            .andExpect(model().attribute("product", hasProperty("stock", is("40"))))
            .andExpect(model().attribute("product", hasProperty("description", is("pequeñito jeje"))))
            .andExpect(view().name("products/createOrUpdateProductForm"))
            .andExpect(status().isOk());
    }
}
