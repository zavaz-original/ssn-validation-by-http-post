package com.sb_juhav.siili_pic;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sb_juhav.siili_pic.service.PicValidationService;
import com.sb_juhav.siili_pic.validation.PicFin;
import com.sb_juhav.siili_pic.validation.DTO.PicValidationRequestDTO;



@RestController
public class Controller {

	@Autowired	
	private PicFin picFin;

	@Autowired	
	private PicValidationService picValidationService;

	/* Specified as:
	 POST validate_ssn
		Request:
			ssn as input parameter in JSON body
			country_code  // FI supported currently
		Response
			ssn_valid (true/false)
	error handling!
	 */

	// POSTMAN:
	// http://localhost:8080/validate_ssn
	/* body:

	{
		"country":"FIN",
		"ssn": "131052-308T"
	}

	 */

	@PostMapping("/validate_ssn")
	public boolean process(@RequestBody Map<String, Object> map) 
			throws Exception {
		PicValidationRequestDTO picValidationRequestDTO = new PicValidationRequestDTO(map);
		return picValidationService.getPicValidationResult( picValidationRequestDTO );
	}

	/*
	// for development used GET from browser before POST
	@GetMapping(value="/validate_ssn")
	public String validatePic( 
			@RequestParam(value="ssn", required=true) String ssn,
			@RequestParam(value="country", required=true) String country )
	{
		if ( country.toUpperCase().equals("FIN") ) { 
			return "" + picFin.isOfValidFormat(ssn);  
		}

		return "Only country=FIN supported at the moment";
	}
	 */

	public Controller()
			throws Exception
	{
		if ( picFin == null ) {
			System.out.println("Controller trying to create PicFin");
			picFin = new PicFin();
			System.out.println("Controller created PicFin");
		}

		if ( picValidationService == null ) {
			System.out.println("Controller trying to create PicValidationService");
			picValidationService = new PicValidationService();
			System.out.println("Controller created PicValidationService");
		}	
	}

}
