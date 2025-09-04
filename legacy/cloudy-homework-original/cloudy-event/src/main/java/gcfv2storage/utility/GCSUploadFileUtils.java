package gcfv2storage.utility;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GCSUploadFileUtils {

    public static byte[] download(String storageUri) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.fromGsUtilUri(storageUri);
        return storage.readAllBytes(blobId);
    }


    public static void upload(byte[] file, String storageUri) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId id = BlobId.fromGsUtilUri(storageUri);
        BlobInfo info = BlobInfo.newBuilder(id).setContentType("jpg").build();
        storage.create(info, file);
    }
}
