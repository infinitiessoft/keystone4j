package com.infinities.keystone4j.common;

/*
 * Base class for all configuration options
 * 
 * An opt object has no public methods, but has a number of public string
 * properties:
 * 
 * name: the name of the option, which may includes hyphens. dest:
 * the(hyphen-less) ConfigOpts property which contains the option value.
 * short: a single character CLI option name default: the default value of
 * the option. positional: True if the option is a positional CLI argument.
 * metaver: the name shown as the argument to a CLI option in --help output
 * help: an string explaining how the options value is used.
 */
public class DeprecatedOpt {

	private String name;
	private String group;


	protected DeprecatedOpt(String name, String group) {
		this.name = name;
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeprecatedOpt other = (DeprecatedOpt) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static DeprecatedOpt deepCopy(DeprecatedOpt orig) {
		return new DeprecatedOpt(orig.getName(), orig.getGroup());
	}
}
