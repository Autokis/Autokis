package ua.com.autokis.application.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.autokis.application.mapper.dd.impl.ProductStatus;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    private String model;
    private String sku;
    private String upc;
    private String ean;
    private String jan;
    private String isbn;
    private String mpn;
    private String location;
    private Integer quantity;
    private String stockStatusId;
    private String image;
    private String manufacturerId;
    private String shipping;
    private String optionsBuy;
    private double price;
    private String points;
    private String taxClassId;
    private String dateAvailable;
    private String weight;
    private String weightClassId;
    private String length;
    private String width;
    private String height;
    private String lengthClassId;
    private String subtract;
    private String minimum;
    private String sortOrder;
    private String status;
    private String viewed;
    private String dateAdded;
    private String dateModified;
    private String importBatch;
    private String noindex;
    private String manufacturer;
    private String languageId;
    private String name;
    private String description;
    private String shortDescription;
    private String tag;
    private String metaTitle;
    private String metaH1;
    private String metaDescription;
    private String metaKeyword;
    private String priceSpecial;
    @Enumerated
    private ProductStatus stockStatus;
    private String link;
    private String store;
    private String rewardGroupOne;
    @ElementCollection
    @CollectionTable(name = "product_images")
    private List<String> additionalImages;
    private String productFilter;
    private String productAttribute;
    private String productCategory;
    private String productDiscount;
    private String productSpecial;
    private String specialPriceForGroupOne;
    private String specialPriceForGroupOneStart;
    private String specialPriceForGroupOneEnd;
    private String relatedProductId;
    private String relatedProductName;
    private String productOption;
}
