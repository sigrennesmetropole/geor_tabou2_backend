package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class Plui {
    private Double densiteOap;

    private String pluiDisposition;

    private String pluiAdaptation;
}
