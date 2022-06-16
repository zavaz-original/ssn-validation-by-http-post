package com.sb_juhav.siili_pic.validation;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */


abstract class Pic implements Pic_I<String> {
	private String country;
	private String pic;
		
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the Pic
	 */
	public String getPic() {
		return pic;
	}

	/**
	 * @param Pic the Pic to set
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public abstract boolean isOfValidFormat(String pic);
}
