package DomainLayer.SystemManagement.NotificationManager;

import DomainLayer.StoreFacade;

public class NotificationFacade {
    private StoreFacade storeFacade;
    private static class NotificationFacadeHolder {
        static final NotificationFacade INSTANCE = new NotificationFacade();
    }
    private NotificationFacade() {
        //this.storeFacade = StoreFacade.getInstance();
    }

    public static NotificationFacade getInstance() {
        return NotificationFacadeHolder.INSTANCE;
    }


    /*public void notifyOwners(int store_id)
    {
        Store store = storeFacade.getStore(store_id);
        List<String> store_owners = store.getOwnersNames();
    }*/


}
