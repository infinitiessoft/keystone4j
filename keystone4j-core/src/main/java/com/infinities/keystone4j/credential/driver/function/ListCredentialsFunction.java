package com.infinities.keystone4j.credential.driver.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.jpa.impl.CredentialDao;
import com.infinities.keystone4j.model.credential.Credential;

public class ListCredentialsFunction implements ListFunction<Credential> {

	@Override
	public List<Credential> execute(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return new CredentialDao().listCredential(hints);
	}

}
