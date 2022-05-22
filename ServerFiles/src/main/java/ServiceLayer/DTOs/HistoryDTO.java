package ServiceLayer.DTOs;

import DomainLayer.SystemManagement.HistoryManagement.History;
import DomainLayer.SystemManagement.HistoryManagement.ItemHistory;

import java.util.HashSet;
import java.util.Set;

public class HistoryDTO {
    public Set<ItemHistoryDTO> items;

    public HistoryDTO(History history) {
        this.items = new HashSet<>();
        for (ItemHistory item : history.getHistoryItems()) {
            this.items.add(new ItemHistoryDTO(item));
        }
    }

    public HistoryDTO() {

    }
}
