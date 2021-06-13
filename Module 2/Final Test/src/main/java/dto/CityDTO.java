package dto;

import java.io.Serializable;
import java.util.Map;

public class CityDTO implements Serializable {
    private long id;
    private String name;
    private int foundationYear;
    private long square;
    private Map<Long, Long> populations;

    public CityDTO() {}

    public CityDTO(long id, String name, int foundationYear, long square, Map<Long, Long> populations) {
        this.id = id;
        this.name = name;
        this.foundationYear = foundationYear;
        this.square = square;
        this.populations = populations;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFoundationYear() {
        return foundationYear;
    }

    public long getSquare() {
        return square;
    }

    public Map<Long, Long> getPopulations() {
        return populations;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    public void setSquare(long square) {
        this.square = square;
    }

    public void setPopulations(Map<Long, Long> populations) {
        this.populations = populations;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("City{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", foundation year=").append(foundationYear)
                .append(", square=").append(square)
                .append(", populations={");
        for (Map.Entry<Long, Long> entry : populations.entrySet()) {
            builder.append("{").append(entry.getKey()).append(", ").append(entry.getValue()).append("} ");
        }
        builder.append("}}");
        return builder.toString();
    }
}