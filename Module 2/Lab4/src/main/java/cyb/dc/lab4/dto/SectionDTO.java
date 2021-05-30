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
public class SectionDTO implements Serializable {
    private long id;
    private String name;

    @Override
    public String toString() {
        return "Section ID: " + id +
                "\nSection name: " + name +
                "\n\n";
    }
}
