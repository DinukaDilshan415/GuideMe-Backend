package me.dinuka.guideme.dto;

public class FacilityDTO {
    private int id;
    private String type;
    private String icon;

    public FacilityDTO() {
    }
    public FacilityDTO(int id, String type, String icon) {
        this.id = id;
        this.type = type;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
