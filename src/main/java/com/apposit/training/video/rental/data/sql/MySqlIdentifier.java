package com.apposit.training.video.rental.data.sql;

/**
 * @author Eric Chijioke
 *
 */
public class MySqlIdentifier implements SQLIdentifier {

	@Override
	public String getIdQuery() {

		return "select last_insert_id()";
	}

}
