package com.infinities.keystone4j.model.credential.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;

public class CredentialWrapper implements MemberWrapper<Credential> {

	private Credential credential;


	public CredentialWrapper() {

	}

	public CredentialWrapper(Credential credential) {
		super();
		this.credential = credential;
	}

	@Override
	public void setRef(Credential ref) {
		this.credential = ref;
	}

	@XmlElement(name = "credential")
	@Override
	public Credential getRef() {
		return credential;
	}

}
