package com.apposit.training.video.rental.data.meta;


/**
 * Concrete implementations of this interface are responsible for buidling metaGraphs from concrete DataSource implementations
 * Typically, each DataSource implementation will have a corresponding MetaGraphFactory implementation.
 * 
 * @author Eric Chijioke
 *
 */
public interface MetaGraphLoader {

	/**
	 * Load a MetaGraph from a data source
	 * @param dataSource
	 * @return a loaded MetaGraph
	 */
	public MetaGraph loadMetaGraph(MetaDataSource dataSource);
	
	/**
	 * Set the MetaGraphProvider which will be used for constructing instances of a MetaGraph object 
	 * @param metaGraphProvider
	 */
	public void setMetaGraphProvider(MetaGraphProvider metaGraphProvider);
	
}
