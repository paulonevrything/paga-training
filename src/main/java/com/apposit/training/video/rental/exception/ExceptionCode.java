package com.apposit.training.video.rental.exception;

import java.io.Serializable;

/**
 * @author Eric Chijioke
 *
 */
public enum ExceptionCode implements Serializable {

	INTERNAL_ERROR,
	CONFIGURATION_ERROR,
	ETL_ERROR,
	OPERATOR_ERROR,
	PERSISTENCE_ERROR,
	PERSISTENCE_DEADLOCK_ERROR,
	REPORTING_ERROR,
	SECURITY_ACCESS_ERROR,
	WEB_SERVICE_ERROR,
	WORKFLOW_ERROR,
	EVALUATION_ERROR,
	UI_ERROR,
	MAIL_ERROR,
	CHARTING_ERROR,
	REMOTING_ERROR;
}
