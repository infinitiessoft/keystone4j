package com.infinities.keystone4j.assignment.action.project;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Project;

public class ListProjectsAction extends AbstractProjectAction<List<Project>> {

	private final String domainid;
	private final String name;
	private final Boolean enabled;


	public ListProjectsAction(AssignmentApi assignmentApi, String domainid, String name, Boolean enabled) {
		super(assignmentApi);
		this.domainid = domainid;
		this.name = name;
		this.enabled = enabled;
	}

	@Override
	public List<Project> execute() {
		Iterable<Project> projects = this.getAssignmentApi().listProjects();

		List<Predicate<Project>> filters = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(domainid)) {
			Predicate<Project> filter = new Predicate<Project>() {

				@Override
				public boolean apply(Project p) {
					return domainid.equals(p.getDomain().getId());
				}
			};
			filters.add(filter);
		}

		if (!Strings.isNullOrEmpty(name)) {
			Predicate<Project> filter = new Predicate<Project>() {

				@Override
				public boolean apply(Project p) {
					return name.equals(p.getName());
				}
			};
			filters.add(filter);
		}

		if (enabled != null) {
			Predicate<Project> filter = new Predicate<Project>() {

				@Override
				public boolean apply(Project p) {
					return enabled.compareTo(p.getEnabled()) == 0;
				}
			};
			filters.add(filter);
		}

		if (filters.size() > 0) {
			Predicate<Project> filter = Predicates.and(filters);

			projects = Iterables.filter(projects, filter);
		}

		return Lists.newArrayList(projects);
	}

	@Override
	public String getName() {
		return "list_projects";
	}
}
