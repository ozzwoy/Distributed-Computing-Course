package cyb.dc.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO implements Serializable {
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
