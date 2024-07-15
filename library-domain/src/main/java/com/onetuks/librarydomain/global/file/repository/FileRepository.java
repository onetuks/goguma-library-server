package com.onetuks.librarydomain.global.file.repository;

import com.onetuks.libraryobject.vo.ImageFile;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository {

  void putFile(ImageFile imageFile);

  void deleteFile(ImageFile imageFile);
}
