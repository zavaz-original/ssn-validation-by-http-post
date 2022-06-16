package com.sb_juhav.siili_pic.validation;

/**
 * @author Juha Valimaki, Siili Candidate by Virnex 2022
 *
 */

public interface Pic_I<T> {

	boolean isOfValidFormat(T pic);

	public static void sout(String s) { 
		System.out.println(s);
	};

	public static void sout() { 
		System.out.println();
	};

}
