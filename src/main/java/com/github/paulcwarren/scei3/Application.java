package com.github.paulcwarren.scei3;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.content.commons.repository.Store;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.content.rest.StoreRestResource;
import org.springframework.content.rest.config.RestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Configuration
	@EnableFilesystemStores
	@Import(RestConfiguration.class)
	public static class AppConfig {

		@Bean
		FileSystemResourceLoader fileSystemResourceLoader() throws IOException {
			return new FileSystemResourceLoader(Files.createTempDirectory("scei3").toFile().getAbsolutePath());
		}
	}

	@StoreRestResource(path="files")
	public interface FileStore extends Store<String> {
	}

}
