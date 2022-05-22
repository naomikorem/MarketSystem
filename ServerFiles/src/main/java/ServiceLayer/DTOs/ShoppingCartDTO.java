package ServiceLayer.DTOs;

import DomainLayer.Users.ShoppingBasket;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartDTO {
    public List<ShoppingBasketDTO> baskets;

    public ShoppingCartDTO(List<ShoppingBasket> baskets) {
        this.baskets = baskets.stream().map(ShoppingBasketDTO::new).collect(Collectors.toList());
    }

    public ShoppingCartDTO() {

    }
}
