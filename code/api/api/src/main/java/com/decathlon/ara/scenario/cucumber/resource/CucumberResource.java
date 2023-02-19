/******************************************************************************
 * Copyright (C) 2020 by the ARA Contributors                                 *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 * 	 http://www.apache.org/licenses/LICENSE-2.0                               *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 *                                                                            *
 ******************************************************************************/

package com.decathlon.ara.scenario.cucumber.resource;

import com.decathlon.ara.scenario.cucumber.upload.CucumberScenarioUploader;
import com.decathlon.ara.service.ProjectService;
import com.decathlon.ara.service.exception.BadRequestException;
import com.decathlon.ara.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.decathlon.ara.scenario.cucumber.resource.CucumberResource.CUCUMBER_SCENARIO_BASE_API_PATH;
import static com.decathlon.ara.web.rest.ProjectResource.PROJECT_CODE_BASE_API_PATH;

@RestController
@RequestMapping(CUCUMBER_SCENARIO_BASE_API_PATH)
public class CucumberResource {

    private static final Logger LOG = LoggerFactory.getLogger(CucumberResource.class);

    public static final String CUCUMBER_SCENARIO_BASE_API_PATH = PROJECT_CODE_BASE_API_PATH + "/cucumber";
    public static final String CUCUMBER_SCENARIO_ALL_API_PATHS = CUCUMBER_SCENARIO_BASE_API_PATH + "/**";

    private final ProjectService projectService;

    private final CucumberScenarioUploader cucumberScenarioUploader;

    public CucumberResource(ProjectService projectService, CucumberScenarioUploader cucumberScenarioUploader) {
        this.projectService = projectService;
        this.cucumberScenarioUploader = cucumberScenarioUploader;
    }

    /**
     * POST to move upload the scenario set of a test code.
     *
     * @param projectCode the code of the project in which to work
     * @param sourceCode  the source-code determining the location of the files that are uploaded
     * @param json        the report.json file as generated by a cucumber --dry-run
     * @return OK on success, INTERNAL_SERVER_ERROR on processing error
     */
    @PostMapping("scenarios/upload/{sourceCode}")
    public ResponseEntity<Void> uploadScenarios(@PathVariable String projectCode, @PathVariable String sourceCode, @Valid @RequestBody String json) {
        LOG.info("SCENARIO|Uploading Cucumber scenarios (source: {}) for project {}", sourceCode, projectCode);
        try {
            cucumberScenarioUploader.uploadCucumber(projectService.toId(projectCode), sourceCode, json);
            return ResponseEntity.ok().build();
        } catch (BadRequestException e) {
            LOG.error("SCENARIO|Failed to upload Cucumber scenarios for source code {}", sourceCode, e);
            return ResponseUtil.handle(e);
        }
    }

}
