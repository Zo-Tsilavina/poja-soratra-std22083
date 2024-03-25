package hei.school.soratra.service.event.soratra;

import hei.school.soratra.file.BucketComponent;
import hei.school.soratra.repository.model.TxtFile;
import jakarta.ws.rs.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
@AllArgsConstructor
public class TxtFileService {

    BucketComponent bucketComponent;
    TxtFileService txtFileService;

    public File uploadTxtFile(String id, byte[] file) {
        try {
            if (file == null) {
                throw new BadRequestException("File is mandatory");
            }
            String fileSuffix = ".txt";
            String inputFilePrefix = id + fileSuffix;
            File tmpFile;
            try {
                tmpFile = File.createTempFile(inputFilePrefix, fileSuffix);
            } catch (IOException e) {
                throw new RuntimeException("Creation of temp file failed");
            }
            writeFileFromByteArray(file, tmpFile);
            String bucketKey = getTxtFileBucketName(inputFilePrefix);
            uploadFile(tmpFile, bucketKey);
            TxtFile toSave = new TxtFile(id, bucketKey);
            txtFileService.save(toSave);
            return bucketComponent.download(bucketKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadFile(File file, String bucketKey) {
        bucketComponent.upload(file, bucketKey);
        file.delete();
    }

    private String getTxtFileBucketName(String filename) {
        return "txt/" + filename;
    }

    public TxtFile getRestTxtFile(TxtFile txtFile) {
        try {
            TxtFile restTxtFile = new TxtFile();
            // Assuming you still want to provide URLs for uploaded files
            TxtFile.setTxt_url(bucketComponent.presign(txtFile.getTxtBucketKey(), Duration.ofHours(12)).toString());
            return restTxtFile;
        } catch (Exception e) {
            return null;
        }
    }

    private File writeFileFromByteArray(byte[] bytes, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}