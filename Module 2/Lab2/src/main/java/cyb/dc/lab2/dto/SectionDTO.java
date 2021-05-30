package cyb.dc.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionDTO {
    private long id;
    private String name;

    @Override
    public String toString() {
        return "Section ID: " + id +
                "\nSection name: " + name +
                "\n\n";
    }
}
