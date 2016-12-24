package com.playinitium;

import java.util.List;

public class SchemaEntity {
	private String className;
	private String description;
	private List<SchemaField> fields;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SchemaField> getFields() {
		return fields;
	}

	public void setFields(List<SchemaField> fields) {
		this.fields = fields;
	}
}
