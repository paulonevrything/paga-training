package com.apposit.training.video.rental.data.meta;

/**
 * Classes implementing this interface will return a concrete {@link MetaGraph} object
 * This interface is used by {@link MetaGraphLoader} implementations to obtain a {@link MetaGraph} object
 * allowing for the configuration of different {@link MetaGraph} implementations
 * 
 * @author Eric Chijioke
 *
 */
public interface MetaGraphProvider {

	/**
	 * Get an instance of a MetaGraph object
	 * @return a MetaGraph object
	 */
	public MetaGraph getMetaGraph();
}
