package com.infinities.keystone4j.common;

import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class OptGroup {

	private String name;
	private String title;
	private String help;
	private Map<String, OptEntry> opts;
	private ArgumentParserGroup argumentParserGroup;


	public OptGroup(String name, String title, String help) {
		this.name = name;
		this.title = (Strings.isNullOrEmpty(title) ? name : title) + " options";
		this.help = help;
		this.opts = Maps.newHashMap();
		argumentParserGroup = null;

	}

	public boolean registerOpt(Opt opt, boolean cli) {

		if (isOptRegistered(opts, opt)) {
			return false;
		}

		Map<String, Object> map = Maps.newHashMap();
		map.put("opt", opt);
		map.put("cli", cli);

		OptEntry entry = new OptEntry(opt, cli);
		opts.put(opt.getDest(), entry);

		return true;
	}

	private boolean isOptRegistered(Map<String, OptEntry> opts, Opt opt) {
		if (opts.containsKey(opt.getDest())) {
			if (opts.get(opt.getDest()).getOpt() != opt) {
				throw new IllegalArgumentException("duplicate " + opt.getName());
			}
			return true;
		} else {
			return false;
		}
	}

	public void unregisterOpt(Opt opt) {
		if (opts.containsKey(opt.getDest())) {
			opts.remove(opt.getDest());
		}
	}

	public ArgumentParserGroup getArgumentParserGroup(ArgumentParser parser) {
		if (argumentParserGroup == null) {
			argumentParserGroup = parser.addArgumentGroup(title, help);
		}

		return argumentParserGroup;
	}

	public void clean() {
		argumentParserGroup = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public Map<String, OptEntry> getOpts() {
		return opts;
	}

	public void setOpts(Map<String, OptEntry> opts) {
		this.opts = opts;
	}

	public ArgumentParserGroup getArgumentParserGroup() {
		return argumentParserGroup;
	}

	public void setArgumentParserGroup(ArgumentParserGroup argumentParserGroup) {
		this.argumentParserGroup = argumentParserGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argumentParserGroup == null) ? 0 : argumentParserGroup.hashCode());
		result = prime * result + ((help == null) ? 0 : help.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((opts == null) ? 0 : opts.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		OptGroup other = (OptGroup) obj;
		if (argumentParserGroup == null) {
			if (other.argumentParserGroup != null)
				return false;
		} else if (!argumentParserGroup.equals(other.argumentParserGroup))
			return false;
		if (help == null) {
			if (other.help != null)
				return false;
		} else if (!help.equals(other.help))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (opts == null) {
			if (other.opts != null)
				return false;
		} else if (!opts.equals(other.opts))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
