package DomainLayer.SystemManagement.PurchaseServices;

import DomainLayer.SystemManagement.AbstractExternalService;

public interface IExternalPurchaseService extends AbstractExternalService
{
    boolean pay(double amount);
}
