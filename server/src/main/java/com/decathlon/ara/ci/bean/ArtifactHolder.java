package com.decathlon.ara.ci.bean;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactHolder {

    /**
     * List of artifacts of the build.
     */
    private List<Artifact> artifacts;

}
