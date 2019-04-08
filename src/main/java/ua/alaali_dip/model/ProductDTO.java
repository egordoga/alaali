package ua.alaali_dip.model;

import lombok.Data;
import ua.alaali_dip.entity.Product;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private Integer quantity;
    private Integer cost;
    private Integer summ;

    public static ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setQuantity(product.getQuantityForOder());
        dto.setCost(product.getCost());
        dto.setSumm(product.getQuantityForOder() * product.getCost());
        return dto;
    }
}
