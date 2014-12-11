package com.infinities.keystone4j.model.policy;

import java.util.HashMap;
import java.util.Map;

public class TargetWrapper {

	private Map<String, PolicyEntity> target = new HashMap<String, PolicyEntity>();


	public Map<String, PolicyEntity> getTarget() {
		return target;
	}

	public void setTarget(Map<String, PolicyEntity> target) {
		this.target = target;
	}

}
