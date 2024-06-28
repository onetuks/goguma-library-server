package com.onetuks.libraryobject;

import com.onetuks.libraryobject.enums.ImageType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileFixture {

  public static MultipartFile create(ImageType imageType, String fileName) {
    try {
      Path path = getTestFilePath(imageType, fileName);
      Files.createDirectories(path.getParent());
      if (!Files.exists(path)) {
        Files.createFile(path);
      }
      byte[] content = Files.readAllBytes(path);
      return new MockMultipartFile(fileName, content);
    } catch (IOException e) {
      return null;
    }
  }

  private static Path getTestFilePath(ImageType imageType, String fileName) {
    return Paths.get("src/test/resources/static" + imageType.getDirectoryPath() + fileName);
  }
}
