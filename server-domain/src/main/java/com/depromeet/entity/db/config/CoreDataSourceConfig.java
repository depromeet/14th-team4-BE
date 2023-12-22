package com.depromeet.entity.db.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class CoreDataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "storage.datasource.core")
	public HikariConfig coreHikariConfig() {
		return new HikariConfig();
	}

	@Bean
	public HikariDataSource coreDataSource(@Qualifier("coreHikariConfig") HikariConfig hikariConfig) {
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}
}
