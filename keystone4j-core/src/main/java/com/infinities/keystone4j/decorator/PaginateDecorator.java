package com.infinities.keystone4j.decorator;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.FilterProtectedAction;

public class PaginateDecorator<V> implements FilterProtectedAction<List<V>> {

	private final int page;
	private final int perPage;
	private final FilterProtectedAction<List<V>> command;
	private final Logger logger = LoggerFactory.getLogger(PaginateDecorator.class);


	public PaginateDecorator(FilterProtectedAction<List<V>> command, int page, int perPage) {
		this.page = page;
		this.perPage = perPage;
		this.command = command;
	}

	@Override
	public List<V> execute(ContainerRequestContext request, String... filters) throws Exception {
		List<V> list = command.execute(request, filters);
		int fromIndex = perPage * (page - 1);
		int toIndex = perPage * page;
		toIndex = toIndex >= list.size() ? list.size() : toIndex;
		logger.debug("fromIndex: {}, toIndex: {}", new Object[] { fromIndex, toIndex });
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public String getName() {
		return command.getName();// "paginate";
	}

}
