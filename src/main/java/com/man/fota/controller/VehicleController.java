package com.man.fota.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.man.fota.dto.feature.FeatureDTO;
import com.man.fota.dto.feature.FeatureListDTO;
import com.man.fota.dto.feature.VehicleFeaturesListDTO;
import com.man.fota.dto.vehicle.VehicleDTO;
import com.man.fota.dto.vehicle.VehicleListDTO;
import com.man.fota.service.FeatureService;
import com.man.fota.service.VehicleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/vehicles")
@Api(tags = {"fota"}, description = "Query possible features per vehicle and possible vehicles per feature")
public class VehicleController extends GenericController {
	
	private static final Logger logger = LogManager.getLogger(VehicleController.class);
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private FeatureService featureService;
	
	@GetMapping
	@ApiOperation(value = "Find all vehicles")
	public ResponseEntity<VehicleListDTO> getAllVehicles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
		logger.info("Starting getting all vehicles");
		Page<VehicleDTO> vehiclePage = vehicleService.getAllVehicles(PageRequest.of(page, pageSize));
		logger.info("Finishing getting all vehicles");
		return ResponseEntity.ok().headers(setHeaders(vehiclePage)).body(new VehicleListDTO(vehiclePage.getContent()));
	}
	
	@GetMapping("/{vin}/installable")
	@ApiOperation(value = "Find installable features by VIN")
	public ResponseEntity<FeatureListDTO> getInstallables(@PathVariable(required = true) String vin) {
		logger.info("Starting getting all installables for: " + vin);
		List<FeatureDTO> featureList = featureService.getAllInstallablesFeaturesByVin(vin);
		logger.info("Finshing getting all installables for: " + vin);
		return ResponseEntity.ok().body(new FeatureListDTO(featureList));
	}
	
	@GetMapping("/{vin}/incompatible")
	@ApiOperation(value = "Find incompatible features by VIN")
	public ResponseEntity<FeatureListDTO> getIncompatibles(@PathVariable(required = true) String vin) {
		logger.info("Starting getting all incompatibles for: " + vin);
		List<FeatureDTO> featureList = featureService.getAllIncompatibleFeaturesByVin(vin);
		logger.info("Starting getting all incompatibles for: " + vin);
		return ResponseEntity.ok().body(new FeatureListDTO(featureList));
	}
	
	@GetMapping("/{vin}")
	@ApiOperation(value = "Find all features by VIN")
	public ResponseEntity<VehicleFeaturesListDTO> getAllFeatures(@PathVariable(required = true) String vin) {
		logger.info("Starting getting all features (either installable or incompatible for: " + vin);
		VehicleFeaturesListDTO featureList = featureService.getAllFeaturesByVin(vin);
		logger.info("Finshing getting all features (either installable or incompatible for: " + vin);
		return ResponseEntity.ok().body(featureList);
	}

}
