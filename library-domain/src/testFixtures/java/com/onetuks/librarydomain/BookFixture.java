package com.onetuks.librarydomain;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.libraryobject.ImageFileFixture;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ImageType;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class BookFixture {

  private static final Random random = new Random();
  private static final List<String> TITLES =
      List.of("난장이가 쏘아올린 작은공", "사도세자의 소원", "신의 물방울", "정글만리", "1Q84", "군주론", "오사카");
  private static final List<String> AUTHOR_NAMES =
      List.of("빠니보틀", "곽튜브", "침착맨", "허니콤보", "김용명", "궤도", "셜록현준", "조승연", "별별역사");

  public static Book create(Long bookId) {
    return new Book(
        bookId,
        createTitle(),
        createAuthorName(),
        "소개글입니다.",
        createIsbn(),
        createPublisher(),
        createCategory(),
        ImageFileFixture.create(ImageType.COVER_IMAGE, UUID.randomUUID().toString()),
        createIsIndie(),
        false);
  }

  private static String createTitle() {
    return TITLES.get(random.nextInt(TITLES.size()));
  }

  private static String createAuthorName() {
    return AUTHOR_NAMES.get(random.nextInt(AUTHOR_NAMES.size())) + "작가";
  }

  private static String createIsbn() {
    return String.valueOf(random.nextLong(1_000_000_000_000L, 9_999_999_999_999L));
  }

  private static String createPublisher() {
    return "출판사" + random.nextLong(10_000L);
  }

  private static List<Category> createCategory() {
    return IntStream.range(0, random.nextInt(1, 4))
        .mapToObj(i -> Category.values()[random.nextInt(Category.values().length)])
        .toList();
  }

  private static boolean createIsIndie() {
    return random.nextBoolean();
  }
}
