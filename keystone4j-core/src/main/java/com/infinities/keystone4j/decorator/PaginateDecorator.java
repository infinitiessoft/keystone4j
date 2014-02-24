package com.infinities.keystone4j.decorator;

import java.util.List;

import com.infinities.keystone4j.Action;

public class PaginateDecorator<V> extends AbstractActionDecorator<List<V>> {

	private int page;
	private int perPage;


	public PaginateDecorator(Action<List<V>> command, int page, int perPage) {
		super(command);
		this.page = page;
		this.perPage = perPage;
	}

	@Override
	public List<V> execute() {
		List<V> list = this.getAction().execute();
		int fromIndex = perPage * (page - 1);
		int toIndex = perPage * page;
		return list.subList(fromIndex, toIndex);
	}
}
