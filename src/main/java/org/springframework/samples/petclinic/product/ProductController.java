package org.springframework.samples.petclinic.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Controller
class ProductController {

    private static final String VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM = "products/createOrUpdateProductForm";

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/products/new")
    public String initCreationForm(Map<String, Object> model) {
        Product product = new Product();
        model.put("product", product);

        return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/products/new")
    public String processCreationForm(@Valid Product product, BindingResult result){
        if(result.hasErrors()){
            return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
        } else {
            this.productRepository.save(product);
            return "redirect:/products/" + product.getId();
        }
    }

    @GetMapping("/products/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("product", new Product());
        return "products/findProducts";
    }

    @GetMapping("/products")
    public String processFindForm(Product product, BindingResult result, Map<String, Object> model) {
        if(product.getName() == null){
            product.setName("");
        }

        Collection<Product> results = this.productRepository.findByName(product.getName());

        if(results.isEmpty()) {
            result.rejectValue("Name", "notFound", "notFound");
            return "products/findProducts";
        } else if(results.size() == 1) {
            product = results.iterator().next();
            return "redirect:/products/" + product.getId();
        } else {
            model.put("selections", results);
            return "products/productsList";
        }
    }

    @GetMapping("/products/{productId}/edit")
    public String initUpdateForm(@PathVariable("productId") int productId, Model model) {
        Product product = this.productRepository.findById(productId);
        model.addAttribute(product);
        return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/products/{productId}/edit")
    public String processUpdateProductForm(@Valid Product product, BindingResult result, @PathVariable("productId") int productId) {
        if(result.hasErrors()) {
            return VIEWS_PRODUCTS_CREATE_OR_UPDATE_FORM;
        } else {
            product.setId(productId);
            this.productRepository.save(product);
            return "redirect:/products/{productId}";
        }
    }

    @GetMapping("/products/{productId}/remove")
    public String processDelete(@PathVariable("productId") int productId, Model model, Product product) {
        product = this.productRepository.findById(productId);
        this.productRepository.delete(product);
        return "redirect:/products/find";
    }

    @GetMapping("products/{productId}")
    public ModelAndView showProduct(@PathVariable("productId") int productId) {
        ModelAndView mav = new ModelAndView("products/productDetails");
        mav.addObject(this.productRepository.findById(productId));
        return mav;
    }

}
