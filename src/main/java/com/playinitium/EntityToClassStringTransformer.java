package com.playinitium;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class EntityToClassStringTransformer {

	/**
	 * 
	 * @param schemaEntity
	 * @return Pair Left=ClassName : Right=ClassContent
	 */
	public Pair<String, String> transform(SchemaEntity schemaEntity) { // TODO - Enums and types on enum values
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

		schemaEntity.getFields().forEach(f -> {
			if (StringUtils.isNotBlank(f.getDescription())) {
				strBuilder.append("// " + f.getDescription() + "\n");
			}
			strBuilder.append("private " + f.getType() + " " + f.getName() + ";\n");
		});

		strBuilder.append("\n");

		schemaEntity.getFields().forEach(f -> {
			strBuilder.append("public void set" + StringUtils.capitalize(f.getName()) + "(" + f.getType() + " " + f.getName() + ") {\n");
			strBuilder.append("this." + f.getName() + " = " + f.getName() + ";\n");
			strBuilder.append("}\n\n");

			strBuilder.append("public " + f.getType() + " get" + StringUtils.capitalize(f.getName()) + "() {\n");
			strBuilder.append("return this." + f.getName() + ";\n");
			strBuilder.append("}\n\n");
		});

		strBuilder.append("}\n");

		return Pair.of(schemaEntity.getClassName(), strBuilder.toString());
	}
}
