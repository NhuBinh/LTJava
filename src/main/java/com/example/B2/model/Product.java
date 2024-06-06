package com.example.B2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;


    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 1, message = "Giá sản phẩm không được thấp hơn 0")
    @Max(value = 99999999, message = "Giá sản phẩm không được cao hơn 9999999")
    private double price;

    private String description;
    @Length(min = 0, max = 50, message = "Tên hình ảnh không được quá 50 ký tự")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}


