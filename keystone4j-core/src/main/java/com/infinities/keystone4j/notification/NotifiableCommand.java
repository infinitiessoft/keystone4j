package com.infinities.keystone4j.notification;

import com.infinities.keystone4j.NonTruncatedCommand;

public interface NotifiableCommand<T> extends NonTruncatedCommand<T> {

	public Object getArgs(int index);
}
