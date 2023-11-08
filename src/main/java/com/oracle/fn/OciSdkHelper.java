package com.oracle.fn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.aispeech.AIServiceSpeechClient;
import com.oracle.bmc.aispeech.model.CreateTranscriptionJobDetails;
import com.oracle.bmc.aispeech.model.ObjectListInlineInputLocation;
import com.oracle.bmc.aispeech.model.ObjectLocation;
import com.oracle.bmc.aispeech.model.OutputLocation;
import com.oracle.bmc.aispeech.requests.CreateTranscriptionJobRequest;
import com.oracle.bmc.aispeech.responses.CreateTranscriptionJobResponse;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;

@Component
public class OciSdkHelper {

	public AuthenticationDetailsProvider getAuthenticationDetailsProvider(String configurationFilePath, String profile)
			throws IOException {
		final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);
		return new ConfigFileAuthenticationDetailsProvider(configFile);
	}

	public ResourcePrincipalAuthenticationDetailsProvider getResourcePrincipalAuthenticationDetailsProvider() {
		return ResourcePrincipalAuthenticationDetailsProvider.builder().build();
	}

	public CreateTranscriptionJobResponse getCreateTranscriptionJobResponse(AbstractAuthenticationDetailsProvider p,
			String compartmentId, String namespaceName, String bucketName, String outBucketName, String fileName) {

		AIServiceSpeechClient client = AIServiceSpeechClient.builder().build(p);

		CreateTranscriptionJobDetails createTranscriptionJobDetails = CreateTranscriptionJobDetails
				.builder().compartmentId(
						compartmentId)
				.inputLocation(ObjectListInlineInputLocation.builder()
						.objectLocations(new ArrayList<>(Arrays
								.asList(ObjectLocation.builder().namespaceName(namespaceName).bucketName(bucketName)
										.objectNames(new ArrayList<>(Arrays.asList(fileName))).build())))
						.build())
				.outputLocation(OutputLocation.builder().namespaceName(namespaceName).bucketName(outBucketName)
						.prefix("result").build())
				.build();

		CreateTranscriptionJobRequest createTranscriptionJobRequest = CreateTranscriptionJobRequest.builder()
				.createTranscriptionJobDetails(createTranscriptionJobDetails).build();

		return client.createTranscriptionJob(createTranscriptionJobRequest);

	}

}
