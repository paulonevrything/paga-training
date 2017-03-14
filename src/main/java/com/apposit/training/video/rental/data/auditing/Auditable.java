package com.apposit.training.video.rental.data.auditing;

import com.apposit.training.video.rental.data.DAOObject;

import java.util.Date;

/**
 * 
 * @author Eric Chijioke
 *
 */
public interface Auditable extends DAOObject {

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy();

	/**
	 * @param updatedBy the lastUpdatedBy to set
	 */
	public void setUpdatedBy(String updatedBy);

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate();

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate);

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy();

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy);

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate();

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate);

}