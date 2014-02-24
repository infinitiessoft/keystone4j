package com.infinities.keystone4j;

public interface Command<T> {

	T execute();
}
