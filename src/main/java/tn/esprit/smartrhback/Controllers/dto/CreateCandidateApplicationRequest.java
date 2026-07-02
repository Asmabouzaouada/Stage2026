package tn.esprit.smartrhback.Controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCandidateApplicationRequest {
    private Long cvId;
    private Long demandeId;
}