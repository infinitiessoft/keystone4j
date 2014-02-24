package com.infinities.keystone4j.common;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class Opt {

	private boolean multi = false;
	private String convertValue = null;
	private String validateValue = null;
	private String name;
	private String dest = null;
	private String shortName = null;
	private String defaultValue = null;
	private boolean positional = false;
	private String metavar = null;
	private String help = null;
	private boolean secret = false;
	private boolean required = false;
	private String deprecatedName = null;
	private String deprecatedGroup = null;
	private List<DeprecatedOpt> deprecatedOpts = Lists.newArrayList();


	protected Opt(String name, String dest, String shortName, String defaultValue, boolean positional, String metavar,
			String help, boolean secret, boolean required, String deprecatedName, String deprecatedGroup,
			List<DeprecatedOpt> deprecatedOpts) {

		this.name = name;
		this.dest = Strings.isNullOrEmpty(dest) ? dest : name.replace('-', '_');
		this.shortName = shortName;
		this.defaultValue = defaultValue;
		this.positional = positional;
		this.metavar = metavar;
		this.help = help;
		this.secret = secret;
		this.required = required;
		this.deprecatedName = Strings.isNullOrEmpty(deprecatedName) ? deprecatedName : deprecatedName.replace('-', '_');

		for (DeprecatedOpt opt : deprecatedOpts) {
			deprecatedOpts.add(DeprecatedOpt.deepCopy(opt));
		}
		deprecatedOpts.add(new DeprecatedOpt(deprecatedName, deprecatedGroup));
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public String getConvertValue() {
		return convertValue;
	}

	public void setConvertValue(String convertValue) {
		this.convertValue = convertValue;
	}

	public String getValidateValue() {
		return validateValue;
	}

	public void setValidateValue(String validateValue) {
		this.validateValue = validateValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isPositional() {
		return positional;
	}

	public void setPositional(boolean positional) {
		this.positional = positional;
	}

	public String getMetavar() {
		return metavar;
	}

	public void setMetavar(String metavar) {
		this.metavar = metavar;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public boolean isSecret() {
		return secret;
	}

	public void setSecret(boolean secret) {
		this.secret = secret;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDeprecatedName() {
		return deprecatedName;
	}

	public void setDeprecatedName(String deprecatedName) {
		this.deprecatedName = deprecatedName;
	}

	public String getDeprecatedGroup() {
		return deprecatedGroup;
	}

	public void setDeprecatedGroup(String deprecatedGroup) {
		this.deprecatedGroup = deprecatedGroup;
	}

	public List<DeprecatedOpt> getDeprecatedOpts() {
		return deprecatedOpts;
	}

	public void setDeprecatedOpts(List<DeprecatedOpt> deprecatedOpts) {
		this.deprecatedOpts = deprecatedOpts;
	}

	// public getFromNamespace(Namespace namespace, String groupName) {
	//
	// }
	//
	// public void addToCli(Parser parser, String groupName) {
	//
	// }
	//
	// public void addToArgparse(Parser parser, Container container, String
	// name,
	// String shortName, String kwargs, String prefix, boolean positional,
	// String deprecatedName) {
	//
	// }
	//
	// public Parser getArgparserContainer(Parser parser,String group){
	//
	// }
	//
	public Map<String, Object> getArgumentParserKwargs(OptGroup group, Map<String, Object> kwargs) {

		if (!this.isPositional()) {
			String dest = this.getDest();
			if (group != null) {
				dest = group.getName() + "_" + dest;
			}
			kwargs.put("dest", dest);
		} else {
			kwargs.put("nargs", "?");
		}
		kwargs.remove("default");
		kwargs.put("metavar", metavar);
		kwargs.put("help", help);
		return kwargs;

	}

	public String getArgparserPrefix(String prefix, String groupName) {
		return Strings.isNullOrEmpty(groupName) ? prefix : groupName + "-" + prefix;
	}

	public String getDeprecatedCliName(String dname, String dgroup, String prefix) {
		if ("DEFAULT".equals(dgroup)) {
			dgroup = null;
		}

		if (Strings.isNullOrEmpty(dname) && Strings.isNullOrEmpty(dgroup)) {
			return null;
		}

		if (Strings.isNullOrEmpty(dname)) {
			dname = this.getName();
		}

		return this.getArgparserPrefix(prefix, dgroup) + dname;
	}

	// private String hyphen(boolean positional, String arg) {
	// return positional ? "" : arg;
	// }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((convertValue == null) ? 0 : convertValue.hashCode());
		result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + ((deprecatedGroup == null) ? 0 : deprecatedGroup.hashCode());
		result = prime * result + ((deprecatedName == null) ? 0 : deprecatedName.hashCode());
		result = prime * result + ((deprecatedOpts == null) ? 0 : deprecatedOpts.hashCode());
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((help == null) ? 0 : help.hashCode());
		result = prime * result + ((metavar == null) ? 0 : metavar.hashCode());
		result = prime * result + (multi ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (positional ? 1231 : 1237);
		result = prime * result + (required ? 1231 : 1237);
		result = prime * result + (secret ? 1231 : 1237);
		result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result + ((validateValue == null) ? 0 : validateValue.hashCode());
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
		Opt other = (Opt) obj;
		if (convertValue == null) {
			if (other.convertValue != null)
				return false;
		} else if (!convertValue.equals(other.convertValue))
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (deprecatedGroup == null) {
			if (other.deprecatedGroup != null)
				return false;
		} else if (!deprecatedGroup.equals(other.deprecatedGroup))
			return false;
		if (deprecatedName == null) {
			if (other.deprecatedName != null)
				return false;
		} else if (!deprecatedName.equals(other.deprecatedName))
			return false;
		if (deprecatedOpts == null) {
			if (other.deprecatedOpts != null)
				return false;
		} else if (!deprecatedOpts.equals(other.deprecatedOpts))
			return false;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (help == null) {
			if (other.help != null)
				return false;
		} else if (!help.equals(other.help))
			return false;
		if (metavar == null) {
			if (other.metavar != null)
				return false;
		} else if (!metavar.equals(other.metavar))
			return false;
		if (multi != other.multi)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (positional != other.positional)
			return false;
		if (required != other.required)
			return false;
		if (secret != other.secret)
			return false;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		if (validateValue == null) {
			if (other.validateValue != null)
				return false;
		} else if (!validateValue.equals(other.validateValue))
			return false;
		return true;
	}

}
