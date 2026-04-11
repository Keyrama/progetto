package it.unifi.swam.cleanlabel.dtos;

public class IngredientDTO {

    private Long id;
    private String name;
    private String eNumber;
    private String description;
    private boolean isArtificial;
    private String riskLevel;

    public IngredientDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getENumber() { return eNumber; }
    public void setENumber(String eNumber) { this.eNumber = eNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isArtificial() { return isArtificial; }
    public void setArtificial(boolean artificial) { isArtificial = artificial; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}
