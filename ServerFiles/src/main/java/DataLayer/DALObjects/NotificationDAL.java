package DataLayer.DALObjects;

import DataLayer.DALObject;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Notifications")
public class NotificationDAL /*implements DALObject<NotificationsKey>*/
{
    @EmbeddedId
    NotificationsKey id;

    //    private String username;
   // private String message;

    public NotificationDAL(NotificationsKey id)
    {
        this.id = id;
    }
    public NotificationDAL() {
    }

    //@Override
    public NotificationsKey getId() {
        return this.id;
    }

    //@Override
    public void setId(NotificationsKey id) {
        this.id.setUsername(id.getUsername());
        this.id.setMessage(id.getMessage());
    }
}
