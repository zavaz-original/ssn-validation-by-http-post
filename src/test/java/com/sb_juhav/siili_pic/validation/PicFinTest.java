package com.sb_juhav.siili_pic.validation;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class PicFinTest {

	@Test
	void testIsOfValidFormatPositive() 
			throws Exception {
		PicFin picFin = new PicFin();
		
		List<String> validPics = List.of(
				"131052-308T",	// Ms Anna Suomalainen, 	DoB in [1900,1999]
				"170797+007V",	// Mr Arvo  Suomalainen, 	DoB year < 1900
				"170797-007V",	// Mr Teemu Suomalainen, 	DoB year in [1900,1999]
				"170707A007M"	// Mr Junnu Suomalainen, 	DoB year >= 2000
				);
		
		int countOfValidPics = 0;
		
		for ( String pic : validPics) {
			if ( picFin.isOfValidFormat(pic) ) {
				 countOfValidPics++;
			}	
		}
		
		assertTrue( countOfValidPics == validPics.size() );	
	}
	
	@Test
	void testIsOfValidFormatNegative() 
			throws Exception {
		PicFin picFin = new PicFin();
		
								// ddMMyy notation in comments for DoB
		List<String> invalidPics = List.of(							
				"131052-308K",	// wrong control character K, mismatch, T expected for Anna Suomalainen
				"131052-308Z",	// wrong control character, 'Z' out of valid control character set
				"170797*007V",	// invalid century *, valid set [+,-,A]
				"170797-007VZ",	// too long
				"170797-007",	// too short
				"320797-007V",	// dd out of range, never such a dd in date
				"310697-007V",	// dd out of range, never such a dd in date
				"290297-3089",	// dd out of Range, no such date that year
				"171797-007V",	// MM out of range, never such a MM in date
				"abcdef-007V",	// garbage in DoB
				"131052-3XYT"	// garbage in individual number, XY
				);
		
		int countOfInvalidPics = 0;
		
		for ( String pic : invalidPics) {
			if ( picFin.isOfValidFormat(pic) == false ) {
				countOfInvalidPics++;
			}	
		}
		
		assertTrue( countOfInvalidPics == invalidPics.size() );	
	}

	// fail("Not yet implemented");

}
