package com.playinitium;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SchemaImporter {
	Path inputFilePath;
	Path outputDirPath;

	public SchemaImporter(Path inputFilePath, Path outputDirPath) {
		this.inputFilePath = inputFilePath;
		this.outputDirPath = outputDirPath;
	}

	public void importSchema() throws IOException {
		List<SchemaEntity> schemaEntityList = new ObjectMapper().readValue(inputFilePath.toFile(), new TypeReference<List<SchemaEntity>>() {});
		schemaEntityList.forEach(this::entityToClass);
	}

	private void entityToClass(SchemaEntity schemaEntity) {
		// TODO
	}
}
