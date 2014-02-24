package com.infinities.keystone4j.policy.check.handler;

import com.infinities.keystone4j.policy.model.Target;

public abstract class AbstractHandler implements Handler {

	private final AbstractHandler next;


	public AbstractHandler(AbstractHandler next) {
		this.next = next;
	}

	protected abstract String getMatch();

	protected abstract boolean isMatch(Target target, String kind);

	@Override
	public boolean handle(String match, String kind, Target target) {
		if (match.equals(getMatch())) {
			return isMatch(target, kind);
		}

		if (next != null) {
			return next.handle(match, kind, target);
		} else {
			return false;
		}
	}
}
