package dto;

import java.io.Serializable;

public class CitizenTypeDTO implements Serializable {
    private long id;
    private long cityId;
    private String name;
    private String language;

    public CitizenTypeDTO() {}

    public CitizenTypeDTO(long id, long cityId, String name, String language) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.language = language;
    }

    public long getId() {
        return id;
    }

    public long getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "CitizenType{" +
                "id=" + id +
                ", city id=" + cityId +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
