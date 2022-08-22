package com.example.demo.api.mq.rabbitmq.encapsulation.producer.config.database;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * @author wudi
 * {@code @PropertySource()}要加载哪个配置文件
 */
@Configuration
@PropertySource({"classpath:rabbit-producer-message.properties"})
public class RabbitProducerDataSourceConfiguration {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RabbitProducerDataSourceConfiguration.class);

	@Value("${rabbit.producer.druid.type}")
	private Class<? extends DataSource> dataSourceType;

	/**
	 * {@code @ConfigurationProperties(prefix = "rabbit.producer.druid.jdbc")}，以rabbit.producer.druid.jdbc为前缀的属性
	 * 都会被注入到DataSource中
	 *
	 * @return 数据源
	 */
	@Bean(name = "rabbitProducerDataSource")
	@Primary
	@ConfigurationProperties(prefix = "rabbit.producer.druid.jdbc")
	public DataSource rabbitProducerDataSource() {
		DataSource rabbitProducerDataSource = DataSourceBuilder.create().type(dataSourceType).build();
		// 打印数据源，看是否注入成功
		LOGGER.info("============= rabbitProducerDataSource : {} ================", rabbitProducerDataSource);
		return rabbitProducerDataSource;
	}

	public DataSourceProperties primaryDataSourceProperties() {
		return new DataSourceProperties();
	}

	@SuppressWarnings("unused")
	public DataSource primaryDataSource() {
		return primaryDataSourceProperties().initializeDataSourceBuilder().build();
	}

}
