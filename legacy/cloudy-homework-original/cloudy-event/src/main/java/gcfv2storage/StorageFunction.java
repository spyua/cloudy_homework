package gcfv2storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.CloudEventsFunction;
import gcfv2storage.service.CompressImageService;
import gcfv2storage.service.impl.CompressImageServiceImpl;
import io.cloudevents.CloudEvent;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class StorageFunction implements CloudEventsFunction {
    private static final Logger logger = Logger.getLogger(StorageFunction.class.getName());

    @Override
    public void accept(CloudEvent event) throws IOException {

        CompressImageService processService = new CompressImageServiceImpl();

        processService.process(event);

    }
}
