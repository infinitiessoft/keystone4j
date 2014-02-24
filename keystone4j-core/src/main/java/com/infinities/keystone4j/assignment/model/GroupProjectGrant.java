package com.infinities.keystone4j.assignment.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.identity.model.Group;

@Entity
@Table(name = "GROUP_PROJECT_GRANT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"GROUPID", "PROJECTID" }) })
public class GroupProjectGrant extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5727136446409993382L;
	private Group group;
	private Project project;
	private Set<GroupProjectGrantMetadata> metadatas = new HashSet<GroupProjectGrantMetadata>(0);


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPID", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECTID", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grant", cascade = CascadeType.ALL)
	public Set<GroupProjectGrantMetadata> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(Set<GroupProjectGrantMetadata> metadatas) {
		this.metadatas = metadatas;
	}

}
