package com.playinitium;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class EntityToClassStringTransformer {

	/**
	 * 
	 * @param schemaEntity
	 * @return Pair Left=ClassName : Right=ClassContent
	 */
	public Pair<String, String> transform(SchemaEntity schemaEntity) {
		StringBuilder strBuilder = new StringBuilder("package com.universeprojects.miniup.server.domain;\n\n");

		if (StringUtils.isNotBlank(schemaEntity.getDescription())) {
			strBuilder.append("//" + schemaEntity.getDescription() + "\n");
		}

		String className = schemaEntity.getClassName().trim();
		strBuilder.append("public class " + className + " {\n\n");
		strBuilder.append("private CachedEntity cachedEntity;\n\n");
		strBuilder.append("public " + className + "() {\nthis.cachedEntity = new CachedEntity(\"" + className + "\");\n}\n\n");
		strBuilder.append("public " + className + "(CachedEntity cachedEntity) {\nthis.cachedEntity = cachedEntity;\n}\n\n");
		strBuilder.append("public CachedEntity getCachedEntity() {\nreturn this.cachedEntity;\n}\n\n");

		String declarations = schemaEntity.getFields().stream().filter(this::isNotEnum).map(this::getFieldText).reduce("", (a, b) -> a + b);
		strBuilder.append(declarations);

		strBuilder.append("\n");

		String enums = schemaEntity.getFields().stream().filter(this::isEnum).map(this::getEnumFieldText).reduce("", (a, b) -> a + b);
		strBuilder.append(enums);

		strBuilder.append("}\n");

		return Pair.of(schemaEntity.getClassName(), strBuilder.toString());
	}

	private String getEnumFieldText(SchemaField field) {
		StringBuilder strBuilder = new StringBuilder();

		String enumValues = field.getDescription().replaceFirst("^.*[(](.*)[)].*$", "$1");
		Pattern isBooleanPattern = Pattern.compile("true,false|false,true", Pattern.CASE_INSENSITIVE);
		boolean isBool = !isBooleanPattern.matcher(enumValues).find();
		String fieldType = isBool ? StringUtils.capitalize(field.getName()) : "Boolean";

		if (isBool) {
			strBuilder.append("private enum " + fieldType + " {\n");
			strBuilder.append(enumValues + ",\n");
			strBuilder.append("}\n\n");
		}
		strBuilder.append("private " + fieldType + " " + field.getName() + ";\n\n");

		return strBuilder.toString() + this.createGetterAndSetter(fieldType, field.getName());
	}

	private boolean isNotEnum(SchemaField field) {
		return !isEnum(field) && !field.getName().startsWith("_");
	}

	private boolean isEnum(SchemaField field) {
		return field.getType().toLowerCase().contains("enum") && !field.getName().startsWith("_");
	}

	private String getFieldText(SchemaField field) {
		String desc = StringUtils.isNotBlank(field.getDescription()) ? "// " + field.getDescription() : StringUtils.EMPTY;
		return desc + "\nprivate " + StringUtils.capitalize(field.getType()) + " " + field.getName() + ";\n\n" + this.createGetterAndSetter(field.getType(), field.getName());
	}

	private String createGetterAndSetter(String type, String name) {
		return createSetter(type, name) + createGetter(type, name);
	}

	private String createSetter(String type, String name) {
		return "public void set" + StringUtils.capitalize(name) + "(" + StringUtils.capitalize(type) + " " + name + ") {\n " + "this." + name + " = " + name + ";\n}\n\n";
	}

	private String createGetter(String type, String name) {
		return "public " + StringUtils.capitalize(type) + " get" + StringUtils.capitalize(name) + "() {\n return this." + name + ";\n}\n\n";
	}
}
