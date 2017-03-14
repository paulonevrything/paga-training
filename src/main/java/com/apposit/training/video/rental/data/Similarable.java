package com.apposit.training.video.rental.data;


/**
 * @author Eric Chijioke
 *
 */
public interface Similarable {

	/**
	 * Return true if the the argument object is "similar" to the current one. Similar means that all properties are the
	 * same except the id and guid
	 */
	public boolean isSimilar(AbstractObject object);
}
