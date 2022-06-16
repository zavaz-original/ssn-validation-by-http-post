
package com.sb_juhav.siili_pic.validation.DTO;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */


import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component public class PicValidationRequestDTO {

	Optional<String> country; Optional<String> pic;

	public PicValidationRequestDTO( Map<String, Object> map ) {

		country = Optional.ofNullable( (String) map.get("country") ); pic =
				Optional.ofNullable( (String) map.get("ssn") );

	}

	public Optional<String> getCountry() { return country; }

	public Optional<String> getPic() { return pic; }

	@Override public String toString() { return
			"PicValidationRequestDTO [country=" + country + ", pic=" + pic + "]"; }

}

