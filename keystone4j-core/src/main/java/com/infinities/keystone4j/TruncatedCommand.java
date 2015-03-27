package com.infinities.keystone4j;

import java.util.List;

import com.infinities.keystone4j.common.Hints;

public interface TruncatedCommand<T> extends Command {

	List<T> execute(Hints hints) throws Exception;

}