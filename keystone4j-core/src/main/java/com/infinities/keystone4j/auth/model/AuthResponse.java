package com.infinities.keystone4j.auth.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AuthResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4935169630065011911L;
	private List<String> methods;
	private Map<String, Object> extras;
	@XmlElement(name = "user_id")
	private String userid;


	public AuthResponse() {
		methods = Lists.newArrayList();
		extras = Maps.newHashMap();
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
