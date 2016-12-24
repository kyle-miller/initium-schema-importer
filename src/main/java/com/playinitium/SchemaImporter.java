package com.playinitium;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SchemaImporter {
	private Path inputFilePath;
	private Path outputDirPath;
	private EntityToClassStringTransformer transformer = new EntityToClassStringTransformer();

	public SchemaImporter(Path inputFilePath, Path outputDirPath) {
		this.inputFilePath = inputFilePath;
		this.outputDirPath = outputDirPath;
	}

	public boolean importSchema() throws IOException {
		List<SchemaEntity> schemaEntityList = new ObjectMapper().readValue(inputFilePath.toFile(), new TypeReference<List<SchemaEntity>>() {});
		return schemaEntityList.stream().map(transformer::transform).map(this::writeClassFile).noneMatch(Boolean.FALSE::equals);
	}

	private boolean writeClassFile(Pair<String, String> classToWrite) {
		try {
			Files.write(outputDirPath.resolve(classToWrite.getLeft() + ".java"), classToWrite.getRight().getBytes(Charset.defaultCharset())); // Default is overwrite
			return true;
		} catch (Exception e) {
			System.out.printf("Failed to write %s to file. Error message: %s\n", classToWrite.getLeft(), e.getMessage());
			return false;
		}
	}
}
