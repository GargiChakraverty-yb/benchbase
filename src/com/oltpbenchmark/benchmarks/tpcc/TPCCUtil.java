/*******************************************************************************
 * oltpbenchmark.com
 *  
 *  Project Info:  http://oltpbenchmark.com
 *  Project Members:  	Carlo Curino <carlo.curino@gmail.com>
 * 				Evan Jones <ej@evanjones.ca>
 * 				DIFALLAH Djellel Eddine <djelleleddine.difallah@unifr.ch>
 * 				Andy Pavlo <pavlo@cs.brown.edu>
 * 				CUDRE-MAUROUX Philippe <philippe.cudre-mauroux@unifr.ch>  
 *  				Yang Zhang <yaaang@gmail.com> 
 * 
 *  This library is free software; you can redistribute it and/or modify it under the terms
 *  of the GNU General Public License as published by the Free Software Foundation;
 *  either version 3.0 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 ******************************************************************************/
package com.oltpbenchmark.benchmarks.tpcc;

/*
 * jTPCCUtil - utility functions for the Open Source Java implementation of 
 *    the TPC-C benchmark
 *
 * Copyright (C) 2003, Raul Barbosa
 * Copyright (C) 2004-2006, Denis Lussier
 *
 */

import static com.oltpbenchmark.benchmarks.tpcc.jTPCCConfig.dateFormat;
import static com.oltpbenchmark.benchmarks.tpcc.jTPCCConfig.nameTokens;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.oltpbenchmark.benchmarks.tpcc.pojo.Customer;

public class TPCCUtil {

    /**
     * Creates a Customer object from the current row in the given ResultSet.
     * The caller is responsible for closing the ResultSet.
     * @param rs an open ResultSet positioned to the desired row
     * @return the newly created Customer object
     * @throws SQLException for problems getting data from row
     */
	public static Customer newCustomerFromResults(ResultSet rs)
			throws SQLException {
		Customer c = new Customer();
		// TODO: Use column indices: probably faster?
		c.c_first = rs.getString("c_first");
		c.c_middle = rs.getString("c_middle");
		c.c_street_1 = rs.getString("c_street_1");
		c.c_street_2 = rs.getString("c_street_2");
		c.c_city = rs.getString("c_city");
		c.c_state = rs.getString("c_state");
		c.c_zip = rs.getString("c_zip");
		c.c_phone = rs.getString("c_phone");
		c.c_credit = rs.getString("c_credit");
		c.c_credit_lim = rs.getFloat("c_credit_lim");
		c.c_discount = rs.getFloat("c_discount");
		c.c_balance = rs.getFloat("c_balance");
		c.c_ytd_payment = rs.getFloat("c_ytd_payment");
		c.c_payment_cnt = rs.getInt("c_payment_cnt");
		c.c_since = rs.getTimestamp("c_since");
		return c;
	}

	public static String randomStr(long strLen) {

		char freshChar;		
		StringBuilder freshString = new StringBuilder();
		freshString.setLength(((int)strLen) - 1);
		while (freshString.length() < (strLen - 1)) {

			freshChar = (char) (Math.random() * 128);
			if (Character.isLetter(freshChar)) {
				freshString.append(freshChar);
			}
		}

		return freshString.toString();

	} // end randomStr

	public static String randomNStr(Random r, int stringLength) {
		StringBuilder output = new StringBuilder();
		char base = '0';
		while (output.length() < stringLength) {
			char next = (char) (base + r.nextInt(10));
			output.append(next);
		}
		return output.toString();
	}

	public static String getCurrentTime() {
		return dateFormat.format(new java.util.Date());
	}

	public static String formattedDouble(double d) {
		String dS = "" + d;
		return dS.length() > 6 ? dS.substring(0, 6) : dS;
	}

	// TODO: TPCC-C 2.1.6: For non-uniform random number generation, the
	// constants for item id,
	// customer id and customer name are supposed to be selected ONCE and reused
	// for all terminals.
	// We just hardcode one selection of parameters here, but we should generate
	// these each time.
	private static final int OL_I_ID_C = 7911; // in range [0, 8191]
	private static final int C_ID_C = 259; // in range [0, 1023]
	// NOTE: TPC-C 2.1.6.1 specifies that abs(C_LAST_LOAD_C - C_LAST_RUN_C) must
	// be within [65, 119]
	private static final int C_LAST_LOAD_C = 157; // in range [0, 255]
	private static final int C_LAST_RUN_C = 223; // in range [0, 255]

	public static int getItemID(Random r) {
		return nonUniformRandom(8191, OL_I_ID_C, 1, 100000, r);
	}

	public static int getCustomerID(Random r) {
		return nonUniformRandom(1023, C_ID_C, 1, 3000, r);
	}

	public static String getLastName(int num) {
		return nameTokens[num / 100] + nameTokens[(num / 10) % 10]
				+ nameTokens[num % 10];
	}

	public static String getNonUniformRandomLastNameForRun(Random r) {
		return getLastName(nonUniformRandom(255, C_LAST_RUN_C, 0, 999, r));
	}

	public static String getNonUniformRandomLastNameForLoad(Random r) {
		return getLastName(nonUniformRandom(255, C_LAST_LOAD_C, 0, 999, r));
	}

	public static int randomNumber(int min, int max, Random r) {
		return (int) (r.nextDouble() * (max - min + 1) + min);
	}

	public static int nonUniformRandom(int A, int C, int min, int max, Random r) {
		return (((randomNumber(0, A, r) | randomNumber(min, max, r)) + C) % (max
				- min + 1))
				+ min;
	}

} // end jTPCCUtil
