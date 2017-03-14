package com.apposit.training.video.rental.data.meta;


/**
 * Instantiates and returns and instance of a {@link DefaultMetaGraph}
 * @author Eric Chijioke
 *
 */
public class DefaultMetaGraphProvider implements MetaGraphProvider {

	@Override
	public MetaGraph getMetaGraph() {
	
		return new DefaultMetaGraph();
	}

}
