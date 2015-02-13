package com.infinities.keystone4j.common;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

//keystone.common.driver_hints 20141204
public class Hints {

	private final List<Filter> filters = new ArrayList<Filter>();
	private Limit limit;


	public void addFilter(String name, Object value) {
		addFilter(name, value, "equals", false);
	}

	public void addFilter(String name, Object value, String comparator, boolean caseSensitive) {
		if (Strings.isNullOrEmpty(comparator)) {
			comparator = "equals";
		}
		this.filters.add(new Filter(name, value, comparator, caseSensitive, "filter"));
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	public void setLimit(int limit) {
		setLimit(limit, true);
	}

	public void setLimit(int limit, boolean truncated) {
		this.limit = new Limit(limit, "limit", truncated);
	}


	public class Filter {

		private String name;
		private Object value;
		private String comparator;
		private boolean caseSensitive;
		private String type;


		public Filter(String name, Object value, String comparator, boolean caseSensitive, String type) {
			super();
			this.name = name;
			this.value = value;
			this.comparator = comparator;
			this.caseSensitive = caseSensitive;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getComparator() {
			return comparator;
		}

		public void setComparator(String comparator) {
			this.comparator = comparator;
		}

		public boolean isCaseSensitive() {
			return caseSensitive;
		}

		public void setCaseSensitive(boolean caseSensitive) {
			this.caseSensitive = caseSensitive;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}

	public class Limit {

		private int limit;
		private String type;
		private boolean truncated;


		public Limit(int limit, String type, boolean truncated) {
			this.limit = limit;
			this.type = type;
			this.truncated = truncated;
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isTruncated() {
			return truncated;
		}

		public void setTruncated(boolean truncated) {
			this.truncated = truncated;
		}

	}


	public Filter getExactFilterByName(String name) {
		for (Filter entry : filters) {
			if ("filter".equals(entry.getType()) && name.equals(entry.getName()) && "equals".equals(entry.getComparator())) {
				return entry;
			}
		}
		return null;
	}
}
