package gcfv2storage.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gcfv2storage.service.CompressImageService;
import gcfv2storage.utility.GCSUploadFileUtils;
import gcfv2storage.utility.ImageUtils;
import io.cloudevents.CloudEvent;

import java.io.IOException;
import java.util.Map;

public class CompressImageServiceImpl implements CompressImageService {

    private static final String COMPRESS_BUKET = "img_compress";

    @Override
    public void process(CloudEvent event) throws IOException {

        String eventString = new String(event.getData().toBytes());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(eventString, Map.class);
        String fileName = map.get("name");
        String bucketName = map.get("bucket");
        String sourceUri = "gs://" + bucketName + "/" + fileName;

        byte[] compressFile = ImageUtils.compress(GCSUploadFileUtils.download(sourceUri),0.5);

        String targetUri = "gs://" + COMPRESS_BUKET + "/" + fileName;

        GCSUploadFileUtils.upload(compressFile,targetUri);

    }


}
