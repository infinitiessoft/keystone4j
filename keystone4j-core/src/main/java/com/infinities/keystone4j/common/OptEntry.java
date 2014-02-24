package com.infinities.keystone4j.common;

public class OptEntry {

	private Opt opt;
	private boolean cli;


	public OptEntry(Opt opt, boolean cli) {
		this.setOpt(opt);
		this.setCli(cli);
	}

	public Opt getOpt() {
		return opt;
	}

	public void setOpt(Opt opt) {
		this.opt = opt;
	}

	public boolean isCli() {
		return cli;
	}

	public void setCli(boolean cli) {
		this.cli = cli;
	}

}
