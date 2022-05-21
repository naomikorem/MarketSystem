package ServiceLayer.DTOs;

import DomainLayer.Users.User;

public class UserDTO
{
    public String userName;
    public String firstName;
    public String lastName;
    public String email;
    public ShoppingCartDTO shoppingCart;
    public UserDTO(User user){
        this.userName = user.getName();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.shoppingCart = new ShoppingCartDTO(user.getCartBaskets());
    }

    public UserDTO() {

    }
}
