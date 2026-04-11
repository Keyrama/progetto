package it.unifi.swam.cleanlabel.dtos;

public class NutritionalClaimDTO {

    private Long id;
    private String label;
    private boolean isValidated;
    private boolean isMisleading;
    private String explanation;

    public NutritionalClaimDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isValidated() { return isValidated; }
    public void setValidated(boolean validated) { isValidated = validated; }

    public boolean isMisleading() { return isMisleading; }
    public void setMisleading(boolean misleading) { isMisleading = misleading; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
