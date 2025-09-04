package gcfv2storage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cloudevents.CloudEvent;

import java.io.IOException;

public interface CompressImageService {

    void process(CloudEvent event) throws IOException;
}
