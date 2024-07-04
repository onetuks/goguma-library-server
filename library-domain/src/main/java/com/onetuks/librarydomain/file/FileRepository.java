package com.onetuks.librarydomain.file;

import com.onetuks.libraryobject.vo.ImageFile;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository {

  void putFile(ImageFile imageFile);

  void deleteFile(ImageFile imageFile);
}
