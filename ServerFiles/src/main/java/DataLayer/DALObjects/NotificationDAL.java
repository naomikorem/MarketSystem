package DataLayer.DALObjects;

import DataLayer.DALObject;

public class NotificationDAL implements DALObject<String>
{
    private String username;
    private String message;

    public NotificationDAL() {
    }

    @Override
    public String getId() {
        return getUsername();
    }

    @Override
    public void setId(String id) {
        setUsername(id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
