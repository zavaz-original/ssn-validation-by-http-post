package com.sb_juhav.siili_pic.validation;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.sb_juhav.DateTools.DateFormatCheck;

/*
The smart part is the check in the end of the file:
public boolean isOfValidFormat(String pic)
The ssn is sent into a java8+ Stream with a chain of several filter & lambda.
Only a valid ssn will pop out from the end of the chain.
If the ssn is invalid an Optional Empty will pop out.

personal identity code "pic" used to support internationalization,
instead of Finland specific "ssn" (social security number).

pic=ssn format case Finland (FIN):
ddMMyyYijkC
// Y (capital) indicates Century: < 1900, [1900,...,1999], >= 2000 
// ( Century cannot be denoted by C to avoid duplicate with Control below )
// ijk typically in ["002",...,"899"] in Finland, odd:man, even:female
// i,j,k all in [0,...,9]
// C reserved for Control. Therefore Century above denoted by Y to avoid duplicate.

ddMMyy : DoB without Century indication

ddMMyyYijkC
-----------
ddMMyy+ijkC		170797+007V			// '+' -> YoB < 1900 				for	Mr Usko Suomalainen
ddMMyy-ijkC		170797-007V			// '-' -> YoB in [1900,...,1999]	for	Mr Esko Suomalainen 
ddMMyyAijkC		170707A007M			// 'A' -> YoB > 2000	 			for Mr Junnu Suomalainen

				131052-308T			// '-' -> YoB in [1900,...,1999]	for	Ms Anna Suomalainen
-> DoB of Anna: 13.10.1952 (1952-10-13, Oct 13 1952)

C?
n = ddMMyyijk, n = 131052308 for Ms Anna Suomalainen
C = tab[ n mod 31 ], where (n mod 31) in [0...30] as in table tab below:
for Anna C = tab[ 131052308 mod 31 ] = tab[25] = "T"
tab:	{'0','1','2','3','4','5','6','7','8','9',
		 'A','B','C','D','E','F',
		 'H',
		 'J','K','L','M','N',
		 'P',
		 'R','S','T','U','V','W','X','Y'}

	// pic = ssn = 131052-308T
	//	1	3	1	0	5	2	-	3	0	8	T		// separated
	//	0	1	2	3	4	5	6	7	8	9	10		// index
 */


@Component
public class PicFin extends Pic implements Pic_I<String> {

	/**
	 * case: country = Finland = FIN = Fin
	 * pic = personal identity code = ssn = social security number
	 */

	// VALID EXAMPLES OF PIC {

	public static final String VALID_PIC_CASE1 = "131052-308T"; // Ms Anna Suomalainen, 	DoB in [1900,1999]

	public static final String VALID_PIC_CASE2 = "170797+007V";	// Mr Arvo  Suomalainen, 	DoB year < 1900
	public static final String VALID_PIC_CASE3 = "170797-007V";	// Mr Teemu Suomalainen, 	DoB year in [1900,1999]
	public static final String VALID_PIC_CASE4 = "170707A007M"; // Mr Junnu Suomalainen, 	DoB year >= 2000

	// for test
	public static final List<String> validPicList = List.of( VALID_PIC_CASE1, VALID_PIC_CASE2, VALID_PIC_CASE3, VALID_PIC_CASE4 );

	// VALID EXAMPLES OF PIC }

	// INVALID EXAMPLES OF PIC {

	public static final List<String> invalidPicList = List.of(
			// ddMMyy notation in comments for DoB
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

	// INVALID EXAMPLES OF PIC }

	// CONSTANTS FOR PARSING PIC {

	// a valid pic: 131052-308T	of Ms Anna Suomalainen
	// 
	//	1	3	1	0	5	2	-	3	0	8	T		// separated
	//	0	1	2	3	4	5	6	7	8	9	10		// index

	private static final int DDMMYY_FIRST_CHAR_INDEX = 0;
	private static final int DDMMYY_LAST_CHAR_INDEX  = 5;

	private static final int CENTURY_CHAR_INDEX =  6;

	private static final int INDIVIDUAL_NUMBER_FIRST_CHAR_INDEX = 7;
	private static final int INDIVIDUAL_NUMBER_LAST_CHAR_INDEX  = 9;

	private static final int CONTROL_CHAR_INDEX = 10;

	private static final int VALID_PIC_LENGTH = 11;


	// CONSTANTS FOR PARSING PIC }

	// CONSTANTS FOR VALIDATING PIC {

	// Result = Dividend / Divisor
	// Remainder = Modulus = Mod	which is an integer, a whole number.

	private static final int CONTROL_CHAR_VALIDATION_DIVISOR = 31;

	// CONSTANTS FOR VALIDATING PIC {

	private static final String validCenturiesAsString = "+-A";
	private static final Set<Character> validCenturies = new HashSet<>(); 

	private static final char[] validControlChars = { '0','1','2','3','4','5','6','7','8','9',
			'A','B','C','D','E','F',
			'H',
			'J','K','L','M','N',
			'P',
			'R','S','T','U','V','W','X','Y'};

	private static final String validControlCharactersAsString = new String(validControlChars); 
	private static final Set<Character> validControlCharacters = new HashSet<>();

	// CONSTANTS FOR VALIDATING PIC }

	// Functions {

	// 3 Functions used as a demo on defining functions

	private static final Function<String, String> picToIntegerAsString = (String pic) -> {
		String integerAsString = pic
				.substring(DDMMYY_FIRST_CHAR_INDEX, DDMMYY_LAST_CHAR_INDEX + 1) 
				.concat( pic.substring(INDIVIDUAL_NUMBER_FIRST_CHAR_INDEX, INDIVIDUAL_NUMBER_LAST_CHAR_INDEX + 1) );
		return integerAsString;
	};

	private static final Function<String, Integer> picIntegerStringToInteger = (String pic) -> 
	Integer.valueOf( picToIntegerAsString.apply(pic) );

	private static final Function<String, Character> getValidControlCharacter = (String pic) -> {
		int validationControlSeed = PicFin.picIntegerStringToInteger.apply(pic);
		int validatedIndex =  validationControlSeed % CONTROL_CHAR_VALIDATION_DIVISOR;
		char validControlChar = validControlChars[ validatedIndex ];
		return validControlChar;
	};


	// Functions }

	// Initialize sets of valid characters {

	private static final void initValidCenturies() {
		validCenturiesAsString
		.chars()
		.forEach( c -> validCenturies.add((char) c));
	}

	private static final void initValidControlCharacters() {
		validControlCharactersAsString
		.chars()
		.forEach( c -> validControlCharacters.add((char) c));
	}

	// Check if a string is a valid integer in format

	private static final boolean isValidIntegerFormat(String maybeInt ) {
		boolean valid = false;
		try {
			int validInt = Integer.parseInt(maybeInt);
			valid = true;
		} catch (Exception e) {
			valid = false;
		}
		return valid;
	}

	// Initialize sets of valid characters }

	// Constructors  {

	public PicFin() 
			throws Exception {
		boolean initWasAlreadyDone = true;
		if ( validCenturies.isEmpty() ) {
			initValidCenturies();
			initWasAlreadyDone = false;
		}

		if ( validControlCharacters.isEmpty() ) {
			initValidControlCharacters();
			initWasAlreadyDone = false;
		}

		// Tests within constructor covered now by JUnit test PicFinTest.java
		/*
		boolean somethingWrong = false;

		if ( initWasAlreadyDone == false ) {
			List<String> unexpectedlyNegitiveTestResultPics = getUnexpectedTestResultPics( validPicList , true );
			if ( unexpectedlyNegitiveTestResultPics.isEmpty() == false ) {
				System.out.println("Expectedly valid pics with test result invalid: " + unexpectedlyNegitiveTestResultPics);
				somethingWrong = true;
			}
			List<String> unexpectedlyPositiveTestResultPics = getUnexpectedTestResultPics( invalidPicList , false );
			if ( unexpectedlyPositiveTestResultPics.isEmpty() == false ) {
				System.out.println("Expectedly invalid pics with test result valid: " + unexpectedlyPositiveTestResultPics);
				somethingWrong = true;
			}
			if ( somethingWrong ) {
				throw new Exception("PicFin gave unexpected pic validity check results with standard test cases");
			}
		}
		*/
	}

	public  List<String> getUnexpectedTestResultPics( List<String> testPics , boolean validExpected)
			throws Exception {
		List<String> unexpectedTestResultPics = new ArrayList<>(); 
		if ( testPics == null ) {
			String error1 = "PicFin.testAllPics could not execute with a null list of pics";
			System.out.println(error1);
			throw new Exception(error1);
		}

		if ( testPics.isEmpty() ) {
			String error2 = "PicFin.testAllPics could not execute with an empty list of pics";
			System.out.println(error2);
			throw new Exception(error2);
		}

		int unexpectedResults = 0;

		for ( String pic : testPics) {
			if ( isOfValidFormat(pic) ) {
				if ( validExpected != true ) {
					unexpectedTestResultPics.add(pic);
					unexpectedResults++;
				}
			} else {
				if ( validExpected != false ) {
					unexpectedTestResultPics.add(pic);
					unexpectedResults++;
				}
			}
		}

		String testType = " positive";
		if ( validExpected == false ) {
			testType = " negative ";
		}

		System.out.println("PicFin.testAllPics checked " + testPics.size() + testType + " test cases. Unexpected results: " + unexpectedResults);

		return unexpectedTestResultPics;
	}

	// Constructors  }

	// The beef of the burger {

	public boolean isOfValidFormat(String pic)
	{
		Optional<String> optPic = Optional.ofNullable(pic);

		if ( optPic.isEmpty() ) return false;

		// Below we can deal directly with pic without risk of NullPointerException 

		System.out.println();
		System.out.println("pic to be validated : " + pic );

		// Stream<String> dobValidationFlow = Stream.of(pic)

		// This stream contains only 1 element at a time.
		// The filters help to verify pic validity step by step

		Optional<String> validPic = Stream.of(pic)
				.filter( str -> str.length() == VALID_PIC_LENGTH )
				.filter( str -> validCenturies.contains( str.charAt(CENTURY_CHAR_INDEX) ) )
				.filter( str -> {
					String ddMMyy = str.substring(DDMMYY_FIRST_CHAR_INDEX, DDMMYY_LAST_CHAR_INDEX + 1 );
					if ( PicFin.isValidIntegerFormat( ddMMyy ) == false ) {
						return false;
					}
					return DateFormatCheck.isValidDateString(ddMMyy, "ddMMyy");
				})
				.filter( str -> {
					String individualNumberAsString = str.substring(INDIVIDUAL_NUMBER_FIRST_CHAR_INDEX, INDIVIDUAL_NUMBER_LAST_CHAR_INDEX + 1);
					if ( PicFin.isValidIntegerFormat( individualNumberAsString ) == false ) {
						return false;
					}
					return true;
				})
				.filter( str -> {
					char controlChar 		= str.charAt(CONTROL_CHAR_INDEX);
					char validControlChar	= PicFin.getValidControlCharacter.apply(str).charValue();
					return ( controlChar == validControlChar );
				})
				.findFirst();

		System.out.println("ssn " + pic + " is valid? " +  validPic.isPresent() );
		System.out.println();

		return validPic.isPresent();
	}

	// The beef of the burger }
}	


