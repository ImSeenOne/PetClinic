package org.springframework.samples.petclinic.product.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.product.Product;
import org.springframework.samples.petclinic.product.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;


@Controller
public class FileUploadController{

    public static ProductRepository productRepository;

    @Autowired
    public FileUploadController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products/{id}/upload")
    public String initUploadPage(@PathVariable String id, Map<String, Object> model) {
        Product product = this.productRepository.findById(Integer.parseInt(id));

        if(product == null) {
            return "products/uploadProductImageView";
        }

        model.put("product", product);
        return "products/uploadProductImageView";
    }


    @PostMapping("/products/{id}/upload")
    public String processUploadPage(Map<String, Object> model, @PathVariable("id") String id, @RequestParam("file") MultipartFile file){
        Product product;
        product = this.productRepository.findById(Integer.parseInt(id));

        if(product == null) {
            return "products/uploadProductImageView";
        }

        Path fileNameAndPath;
        String filename = this.buildFilename(product, file);

        try{
            fileNameAndPath = Paths.get(System.getProperty("user.dir")+"/src/main/resources/static/resources/images",  filename);
            Files.write(fileNameAndPath, file.getBytes());
        } catch(IOException ioex) {
            ioex.printStackTrace();
            Product productt = this.productRepository.findById(Integer.parseInt(id));
            model.put("product", productt);
            return "products/uploadProductImageView";
        }

        product.setImagePath(filename);
        this.productRepository.save(product);

        return "redirect:/products/" + product.getId();
    }

    private String buildFilename(Product product, MultipartFile file) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String[] tname = product.getName().split(" ");
        String name = Arrays.toString(tname);
        name = name.substring(1, name.length()-1).replace(",", "");
        String date = dtf.format(now).replace("/","");

        String extension = (file.getOriginalFilename()).substring((file.getOriginalFilename()).lastIndexOf("."));

        return (date + "_" + name + "_" + product.getId()).replace(" ", "-").replace(":","") + extension;
    }
}
