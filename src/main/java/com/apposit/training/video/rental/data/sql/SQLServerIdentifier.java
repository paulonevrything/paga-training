package com.apposit.training.video.rental.data.sql;

/**
 * @author Eric Chijioke
 *
 */
public class SQLServerIdentifier implements SQLIdentifier {

	@Override
	public String getIdQuery() {

		return "SELECT @@IDENTITY";
	}

}
