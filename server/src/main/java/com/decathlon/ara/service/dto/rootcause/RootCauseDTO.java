package com.decathlon.ara.service.dto.rootcause;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Wither
public class RootCauseDTO {

    private Long id;

    @NotNull(message = "The name is required.")
    @Size(min = 1, max = 128, message = "The name is required and must not exceed {max} characters.")
    private String name;

}
