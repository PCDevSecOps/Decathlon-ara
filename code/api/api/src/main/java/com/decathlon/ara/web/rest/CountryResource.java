/******************************************************************************
 * Copyright (C) 2019 by the ARA Contributors                                 *
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

package com.decathlon.ara.web.rest;

import com.decathlon.ara.service.CountryService;
import com.decathlon.ara.service.ProjectService;
import com.decathlon.ara.service.dto.country.CountryDTO;
import com.decathlon.ara.service.dto.support.Upsert;
import com.decathlon.ara.service.dto.support.UpsertResultDTO;
import com.decathlon.ara.service.exception.BadRequestException;
import com.decathlon.ara.service.exception.NotFoundException;
import com.decathlon.ara.web.rest.util.HeaderUtil;
import com.decathlon.ara.web.rest.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.decathlon.ara.Entities.COUNTRY;
import static com.decathlon.ara.web.rest.CountryResource.COUNTRY_BASE_API_PATH;
import static com.decathlon.ara.web.rest.ProjectResource.PROJECT_CODE_BASE_API_PATH;

/**
 * REST controller for managing Countries.
 */
@RestController
@RequestMapping(COUNTRY_BASE_API_PATH)
public class CountryResource {

    public static final String COUNTRY_BASE_API_PATH = PROJECT_CODE_BASE_API_PATH + "/countries";
    public static final String COUNTRY_ALL_API_PATHS = COUNTRY_BASE_API_PATH + "/**";

    private final CountryService service;

    private final ProjectService projectService;

    public CountryResource(CountryService service, ProjectService projectService) {
        this.service = service;
        this.projectService = projectService;
    }

    /**
     * POST to create a new entity.
     *
     * @param projectCode the code of the project in which to work
     * @param dtoToCreate the entity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entity, or with status 400 (Bad
     * Request) if an entity with the same code or name already exists
     */
    @PostMapping
    public ResponseEntity<CountryDTO> create(@PathVariable String projectCode, @Valid @RequestBody CountryDTO dtoToCreate) {
        try {
            CountryDTO createdDto = service.create(projectService.toId(projectCode), dtoToCreate);
            return ResponseEntity
                    .created(HeaderUtil.uri(COUNTRY_BASE_API_PATH + "/" + createdDto.getCode(), projectCode))
                    .headers(HeaderUtil.entityCreated(COUNTRY, createdDto.getCode()))
                    .body(createdDto);
        } catch (BadRequestException e) {
            return ResponseUtil.handle(e);
        }
    }

    /**
     * PUT to update an existing entity OR insert a new one if the code does not exist yet.
     *
     * @param projectCode         the code of the project in which to work
     * @param code                the code of the entity to update
     * @param dtoToInsertOrUpdate the entity to insert or update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entity, or with status 400 (Bad Request) if the entity is not
     * valid, or with status 500 (Internal Server Error) if the entity couldn't be inserted nor updated
     */
    @PutMapping("/{code}")
    public ResponseEntity<CountryDTO> createOrUpdate(@PathVariable String projectCode, @PathVariable String code, @Valid @RequestBody CountryDTO dtoToInsertOrUpdate) {
        dtoToInsertOrUpdate.setCode(code); // HTTP PUT requires the URL to be the URL of the entity
        try {
            final UpsertResultDTO<CountryDTO> result = service.createOrUpdate(projectService.toId(projectCode), dtoToInsertOrUpdate);
            final boolean isNew = result.getOperation() == Upsert.INSERT;
            final String newCode = result.getUpsertedDto().getCode();
            return ResponseEntity
                    .status(isNew ? HttpStatus.CREATED : HttpStatus.OK)
                    .headers(isNew ? HeaderUtil.entityCreated(COUNTRY, newCode) : HeaderUtil.entityUpdated(COUNTRY, newCode))
                    .body(result.getUpsertedDto());
        } catch (BadRequestException e) {
            return ResponseUtil.handle(e);
        }
    }

    /**
     * GET all entities.
     *
     * @param projectCode the code of the project in which to work
     * @return the ResponseEntity with status 200 (OK) and the list of entities in body
     */
    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAll(@PathVariable String projectCode) {
        try {
            return ResponseEntity.ok().body(service.findAll(projectService.toId(projectCode)));
        } catch (NotFoundException e) {
            return ResponseUtil.handle(e);
        }
    }

    /**
     * DELETE one entity.
     *
     * @param projectCode the code of the project in which to work
     * @param code        the code of the entity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String projectCode, @PathVariable String code) {
        try {
            service.delete(projectService.toId(projectCode), code);
            return ResponseUtil.deleted(COUNTRY, code);
        } catch (BadRequestException e) {
            return ResponseUtil.handle(e);
        }
    }

}
