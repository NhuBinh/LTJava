package com.example.B2.controller;

import com.example.B2.model.Product;
import com.example.B2.repository.ProductRepository;
import com.example.B2.service.CategoryService;
import com.example.B2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService; // Đảm bảo bạn đã inject
    // Display a list of all products
    @GetMapping()
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());

        return "products/products-list.html";
    }
    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/add-product";
    }
    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }
        productService.addProduct(product);
        return "redirect:/products";
    }
    // For editing a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/update-product";
    }
    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product,
                                BindingResult result) {
        if (result.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            return "/products/update-product";
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }
    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        products.forEach(product -> product.setImageUrl("/images/" + product.getId() + ".jpg")); // Assuming the image name matches the product ID
        return products;
    }
    @GetMapping("/search")
    public String searchByName(@RequestParam("keyword") String keyword, Model model){
        List<Product> products = productService.getAllProducts()
                .stream()
                .filter(p-> p.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "/products/product-list";
    }

}
