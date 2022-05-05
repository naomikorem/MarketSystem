package DomainLayer.SystemManagement.ExternalServices.PurchaseServices;

import DomainLayer.SystemManagement.ExternalServices.AbstractExternalService;

public interface IExternalPurchaseService extends AbstractExternalService
{
    boolean pay(double amount);
}
