package com.playinitium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		Path inputPath = Paths.get("input");
		
		if (!Files.exists(inputPath) || Files.list(inputPath).count() != 1) {
			System.out.println(String.format("Input file needs to be located in %s", inputPath.toAbsolutePath()));
		}

		Path outputPath = Paths.get("output");
		Files.createDirectories(outputPath);
		System.out.println(String.format("Output files will be located in %s", outputPath.toAbsolutePath()));

		SchemaImporter schemaImporter = new SchemaImporter(Files.list(inputPath).findFirst().get(), outputPath);
		schemaImporter.importSchema();
	}
}
