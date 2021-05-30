package cyb.dc.lab3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private long id;
    private long sectionId;
    private String name;
    private int price;

    @Override
    public String toString() {
        return "Item ID: " + id +
                "\nItem name: " + name +
                "\nPrice: " + price +
                "\n\n";
    }
}
