package DomainLayer;

import DomainLayer.Users.AdminController;

public class AdminFacade {
    private AdminController adminController;

    public AdminFacade() {
        this.adminController = AdminController.getInstance();
    }

    public Response<Boolean> addAdmin(String name) {
        return new Response<>(adminController.addAdmin(name));
    }

    public Response<Boolean> isAdmin(String name) {
        return new Response<>(adminController.isAdmin(name));
    }

    public Response<Boolean> removeAdmin(String name) {
        return new Response<>(adminController.removeAdmin(name));
    }

    public Response<Boolean> hasAdmin() { return new Response<>(adminController.hasAdmin()); }
}
