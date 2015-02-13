package com.infinities.keystone4j;

public interface NonTruncatedCommand<T> extends Command {

	T execute() throws Exception;
}
