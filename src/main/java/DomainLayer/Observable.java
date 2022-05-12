package DomainLayer;

import java.util.List;

public interface Observable
{
    public void attachObserver(Observer observer);
    public void detachObserver(Observer observer);
    public boolean notifyUsers(List<String> observers_to_notify, String message);
}
