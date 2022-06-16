package com.sb_juhav.DateTools;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

// DateFormatCheck.isValidDateStr(String dateAsString, String format)

@Component
public class DateFormatCheck {

	public static final boolean isValidDateString(String dateAsString, String format)
	{
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(dateAsString);
			if (!dateAsString.equals(sdf.format(date))) {
				return false;
			}
			return true;

		} catch (Exception e) {
			System.out.println("Exception " + e + " in Dateformat.isValidDateStr(" + dateAsString + "," + format + ")" );
		}
		return false;
	}

}


