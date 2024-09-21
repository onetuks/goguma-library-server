package com.onetuks.libraryinjector;

import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.libraryauth.util.OnlyForAdmin;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/injector")
public class DataInjectorRestController {

  private final DataInjectorService dataInjectorService;

  public DataInjectorRestController(DataInjectorService dataInjectorService) {
    this.dataInjectorService = dataInjectorService;
  }

  @OnlyForAdmin
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> postData(
      @LoginId Long adminId, @RequestParam("csv") MultipartFile csvFile) {
    dataInjectorService.injectData(adminId, csvFile);

    return ResponseEntity.status(HttpStatus.CREATED).body(Boolean.TRUE);
  }
}
