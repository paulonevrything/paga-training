package com.apposit.training.video.rental.data.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Filter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -372867539383244329L;
	
	protected List<Filter> filters;		//need to use list (order is important)
	
	protected FilterJoin join;
	
	protected String field;
	
	protected Entity entity;
	
	protected FilterComparator comparator;
	
	protected String criterion1;
	
	protected String criterion2;

	public Filter() {

		super();
		filters = new ArrayList<Filter>();
		
	}
	
	@Override
	public String toString() {

		return (field == null ? "" : field) + (comparator == null ? "" : " " + comparator.name()) + (criterion1 == null ? "" : " " + criterion1) + (criterion2 == null ? "" : " AND " + criterion2);
		
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getCriterion1() {
		return criterion1;
	}

	public void setCriterion1(String criterion1) {
		this.criterion1 = criterion1;
	}

	public String getCriterion2() {
		return criterion2;
	}

	public void setCriterion2(String criterion2) {
		this.criterion2 = criterion2;
	}

	public FilterComparator getComparator() {
		return comparator;
	}

	public void setComparator(FilterComparator comparator) {
		this.comparator = comparator;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public FilterJoin getJoin() {
		return join;
	}

	public void setJoin(FilterJoin join) {
		this.join = join;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Filter clone = new Filter();

		List<Filter> filtersClone = new ArrayList<Filter>(filters.size());
		
		for (Filter filter : filters) {
			
			filtersClone.add((Filter) filter.clone());			
		}
		
		clone.join = this.join;
		
		clone.field = this.field;
		
		//TODO: not so sure about this. will probably have to be replaced by cloned entity too
		clone.entity = this.entity;
		
		clone.comparator = this.comparator;
		
		clone.criterion1 = this.criterion1;
		
		clone.criterion2 = this.criterion2;
		
		return clone;
	}

}
