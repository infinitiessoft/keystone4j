package com.infinities.keystone4j.endpointfilter.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.catalog.model.Endpoint;

@Entity
@Table(name = "PROJECTENDPOINT", schema = "PUBLIC", catalog = "PUBLIC")
public class ProjectEndpoint extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3256333939677330953L;
	private Endpoint endpoint;
	private Project project;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENDPOINTID", nullable = false)
	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECTID", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
