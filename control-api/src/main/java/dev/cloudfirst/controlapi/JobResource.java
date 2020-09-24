package dev.cloudfirst.controlapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Job;
import io.kubernetes.client.openapi.models.V1JobList;
import io.kubernetes.client.openapi.models.V1JobSpecBuilder;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1JobBuilder;

@Path("/control-api/jobs")
public class JobResource {
    @Inject
    KubeClient kubeClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> hello() throws ApiException {
        List<String> jobs = new ArrayList<>();

        CoreV1Api api = new CoreV1Api(kubeClient.getApiClient());

        V1PodList list =
        api.listNamespacedPod(System.getenv("POD_NAMESPACE"), null, null, null, "status.phase=Running", null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            jobs.add(item.getMetadata().getName());
        }

        return jobs;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createJob(@RequestBody String name) throws ApiException {
        BatchV1Api api = new BatchV1Api(kubeClient.getApiClient());

        // build job
        V1Job job = new V1JobBuilder()
            .withApiVersion("batch/v1")
            .withKind("Job")
            .withNewMetadata()
                .withName("hello-world-" + UUID.randomUUID())
            .endMetadata()
            .withNewSpec()
                .withNewTemplate()
                    .withNewMetadata()
                        .withName("hello-world")
                    .endMetadata()
                    .withNewSpec()
                        .addNewContainer()
                            .withName("hello-world")
                            .withImage("quay.io/cloudfirst/task-job:latest")
                            .addNewEnv()
                                .withName("POD_NAME")
                                .withNewValueFrom()
                                    .withNewFieldRef()
                                        .withFieldPath("metadata.name")
                                    .endFieldRef()
                                .endValueFrom()
                            .endEnv()
                        .endContainer()
                        .withRestartPolicy("Never")
                    .endSpec()
                .endTemplate()
            .endSpec()
        .build();
        

        try {
            api.createNamespacedJob(System.getenv("POD_NAMESPACE"), job, "true", null, null);
        } catch(ApiException ex) {
            System.out.println(ex.getResponseBody());
            ex.printStackTrace();
        }
        System.out.println("create job");
    }
}