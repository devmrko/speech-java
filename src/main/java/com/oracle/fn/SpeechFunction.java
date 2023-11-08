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
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;

@Configuration
@ComponentScan(basePackages = "com.oracle.fn")
public class SpeechFunction {
	
	private static final Logger logger = LoggerFactory.getLogger(SpeechFunction.class);

	public String handleRequest(String input) throws IOException {
	
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpeechTest.class)) {

			InputParser inputParser = context.getBean(InputParser.class);
			OciSdkHelper ociSdkHelper = context.getBean(OciSdkHelper.class);

			String fileName;
			fileName = inputParser.getParsingValue(input);
			logger.debug("fileName: {}", fileName);

			Resource resource = new ClassPathResource("application.properties");
			Properties properties;
			properties = PropertiesLoaderUtils.loadProperties(resource);

			String namespaceName = System.getenv("namespaceName") == null ? properties.getProperty("namespaceName") : System.getenv("namespaceName");
			String bucketName = System.getenv("bucketName") == null ? properties.getProperty("bucketName") : System.getenv("bucketName");
			String compartmentId = System.getenv("compartmentId") == null ? properties.getProperty("compartmentId") : System.getenv("compartmentId");
			String outBucketName = System.getenv("outBucketName") == null ? properties.getProperty("outBucketName") : System.getenv("outBucketName");

			ResourcePrincipalAuthenticationDetailsProvider p = ociSdkHelper.getResourcePrincipalAuthenticationDetailsProvider();
			CreateTranscriptionJobResponse createTranscriptionJobResponse = ociSdkHelper
					.getCreateTranscriptionJobResponse(p, compartmentId, namespaceName, bucketName, outBucketName,
							fileName);
			logger.info("{}", createTranscriptionJobResponse.get__httpStatusCode__());
			return "Calling Speech AI successfully done.";

		} catch (BeansException e) {
			e.printStackTrace();
			logger.error("BeansException error: {}", e.getMessage());
			return "BeansException error";
			
		} catch (InputParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("InputParseException error: {}", e.getMessage());
			return "InputParseException error";
		}
		
	}

}