package com.infinities.keystone4j.model.token.v2.wrapper;

import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;

public class TokenV2DataWrapper implements MemberWrapper<Access>, ITokenDataWrapper {

	private Access access;


	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

	@Override
	public void setRef(Access ref) {
		this.access = ref;
	}

	@XmlTransient
	@Override
	public Access getRef() {
		return access;
	}

}
