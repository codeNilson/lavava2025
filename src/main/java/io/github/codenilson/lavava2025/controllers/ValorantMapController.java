package io.github.codenilson.lavava2025.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.services.ValorantMapService;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller responsible for Valorant map operations.
 * Provides endpoint to search maps by name.
 *
 * @author codenilson
 * @since 2025-01-01
 */
@Tag(name = "Valorant Maps", description = "API for managing Valorant maps")
@RestController
@RequestMapping("maps")
@RequiredArgsConstructor
public class ValorantMapController {

    private final ValorantMapService valorantMapService;

    /**
     * Retrieves a Valorant map by its name.
     *
     * @param name Name of the map to search for
     * @return The corresponding map, if found
     */
    @Operation(summary = "Get map by name", description = "Returns a Valorant map by its exact name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Map found successfully", content = @Content(schema = @Schema(implementation = ValorantMap.class))),
            @ApiResponse(responseCode = "404", description = "Map not found"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter")
    })
    @GetMapping("/{name}")
    public ResponseEntity<ValorantMap> getMapByName(
            @Parameter(description = "Name of the map to search for", example = "Ascent") @PathVariable String name) {
        ValorantMap map = valorantMapService.findByName(name);
        return ResponseEntity.ok(map);
    }

}
