package com.sb_juhav.siili_pic.service;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sb_juhav.siili_pic.validation.PicFin;
import com.sb_juhav.siili_pic.validation.DTO.PicValidationRequestDTO;

@Component
public class PicValidationService {

	@Autowired
	PicFin picFin;

	public PicValidationService() {
		if ( picFin == null ) {
			try {
				picFin = new PicFin();
			} catch (Exception exception) {
				System.out.println("PicValidationService() constructor threw Exception " + exception);
				System.exit(1);
			}
		}
	}

	public boolean getPicValidationResult(PicValidationRequestDTO picValidationRequestDTO) 
			throws Exception {

		if ( picValidationRequestDTO.getCountry().isEmpty() ) {
			return false;
		}

		if ( picValidationRequestDTO.getPic().isEmpty() ) {
			return false;
		}

		String country = picValidationRequestDTO
				.getCountry()
				.get()
				.toUpperCase();

		String pic = picValidationRequestDTO
				.getPic()
				.get()
				.toUpperCase();

		if ( country.equals("FIN") == false ) {
			return false;
		}

		return picFin.isOfValidFormat( pic );
	}
}

