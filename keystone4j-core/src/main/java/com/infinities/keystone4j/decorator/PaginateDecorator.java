package com.infinities.keystone4j.decorator;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.Action;

public class PaginateDecorator<V> implements Action<List<V>> {

	private final int page;
	private final int perPage;
	private final Action<List<V>> command;


	public PaginateDecorator(Action<List<V>> command, int page, int perPage) {
		this.page = page;
		this.perPage = perPage;
		this.command = command;
	}

	@Override
	public List<V> execute(ContainerRequestContext request) {
		List<V> list = command.execute(request);
		int fromIndex = perPage * (page - 1);
		int toIndex = perPage * page;
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public String getName() {
		return "paginate";
	}
}
