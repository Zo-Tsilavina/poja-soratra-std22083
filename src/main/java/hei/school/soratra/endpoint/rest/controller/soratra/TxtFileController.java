package hei.school.soratra.endpoint.rest.controller.soratra;

import hei.school.soratra.repository.model.TxtFile;
import hei.school.soratra.service.event.soratra.TxtFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@AllArgsConstructor
public class TxtFileController {

    TxtFileService service;

    @PutMapping(value = "/upload-txt/{id}")
    public ResponseEntity<String> uploadTxtFile(
            @PathVariable(name = "id") String id, @RequestBody(required = false) byte[] file) {

        File output = service.uploadTxtFile(id, file);
        if (output == null) {
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("");
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("OK");
    }

    @GetMapping(value = "/txt-file/{id}")
    public TxtFile getTxtFileById(@PathVariable(name = "id") String id) {
        TxtFile txtFile = txtService.getTxtFileById(id);
        TxtFile output = service.getRestTxtFile(txtFile);
        if (output == null) {
            TxtFile restTxtFile = new TxtFile();
            restTxtFile.setTxt_url("https://txt.url");
            return restTxtFile;
        }
        return output;
    }
}
