package org.springframework.samples.petclinic.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class FileUploadController {

    public static ProductRepository productRepository;

    public FileUploadController(ProductRepository productRepository) {this.productRepository = productRepository;}

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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Path fileNameAndPath;
        String[] tname = product.getName().split(" ");
        String name = Arrays.toString(tname);
        name = name.substring(1, name.length()-1).replace(",", "");
        String date = dtf.format(now).replace("/","");

        String extension = (file.getOriginalFilename()).substring((file.getOriginalFilename()).lastIndexOf("."));

        try{
            fileNameAndPath = Paths.get("C:\\Users\\Crist\\Documents\\IntelliJ IDEA Projects\\spring-petclinic-master\\src\\main\\resources\\static\\resources\\images",  (date + "_" + name + "_" + product.getId()).replace(" ", "-").replace(":","") + extension);
            Files.write(fileNameAndPath, file.getBytes());
        } catch(IOException ioex) {
            ioex.printStackTrace();
            Product productt = this.productRepository.findById(Integer.parseInt(id));
            model.put("product", productt);
            return "products/uploadProductImageView";
        }

        product.setImagePath((date + "_" + name + "_" + product.getId()).replace(" ", "-").replace(":","") + extension);
        this.productRepository.save(product);

        return "redirect:/products/" + product.getId();
    }
}
