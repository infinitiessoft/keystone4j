package com.infinities.keystone4j.assignment.action.role.v3;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;

public class ListRolesAction extends AbstractRoleAction<List<Role>> {

	private final String name;


	public ListRolesAction(AssignmentApi assignmentApi, String name) {
		super(assignmentApi);
		this.name = name;
	}

	@Override
	public List<Role> execute() {
		Iterable<Role> roles = this.getAssignmentApi().listRoles();

		List<Predicate<Role>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(name)) {
			Predicate<Role> filter = new Predicate<Role>() {

				@Override
				public boolean apply(Role d) {
					return name.equals(d.getName());
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<Role> filter = Predicates.and(filters);

			roles = Iterables.filter(roles, filter);
		}

		return Lists.newArrayList(roles);
	}

	@Override
	public String getName() {
		return "list_roles";
	}
}
