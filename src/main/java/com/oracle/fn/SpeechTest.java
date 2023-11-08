package com.oracle.fn;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.oracle.bmc.aispeech.responses.CreateTranscriptionJobResponse;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;

@Configuration
@ComponentScan(basePackages = "com.oracle.fn")
public class SpeechTest {

	private static final Logger logger = LoggerFactory.getLogger(SpeechTest.class);

	public static void main(String[] args) throws IOException, InterruptedException {

		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpeechTest.class)) {

			OciSdkHelper ociSdkHelper = context.getBean(OciSdkHelper.class);

			Resource resource = new ClassPathResource("application.properties");
			Properties properties;
			properties = PropertiesLoaderUtils.loadProperties(resource);

			String configurationFilePath = properties.getProperty("configurationFilePath");
			String profile = properties.getProperty("profile");
			String namespaceName = properties.getProperty("namespaceName");
			String bucketName = properties.getProperty("bucketName");
			String compartmentId = properties.getProperty("compartmentId");
			String outBucketName = properties.getProperty("outBucketName");

			String fileName = "eng_m1.wav";

			AuthenticationDetailsProvider p = ociSdkHelper.getAuthenticationDetailsProvider(configurationFilePath,
					profile);
			CreateTranscriptionJobResponse createTranscriptionJobResponse = ociSdkHelper
					.getCreateTranscriptionJobResponse(p, compartmentId, namespaceName, bucketName, outBucketName,
							fileName);
			logger.info("{}", createTranscriptionJobResponse.get__httpStatusCode__());

		} catch (BeansException e) {
			e.printStackTrace();

		}

	}

}
