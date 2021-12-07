package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.DnaSequenceModel;
import com.demo.service.MutantService;

@RestController
@RequestMapping("v1")
public class MutantController {

	@Autowired
	private MutantService mutantService;

	@PostMapping("/mutant")
	public ResponseEntity<Object> isMutant(@RequestBody DnaSequenceModel dna) {
		System.out.println("Llego al servicio");

		boolean isMutant = mutantService.isMutant(dna);
		if (isMutant) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@GetMapping("/stats")
	public String getMutants() {
		return mutantService.getStats();
	}
}
