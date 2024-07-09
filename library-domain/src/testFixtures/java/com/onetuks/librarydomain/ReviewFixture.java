package com.onetuks.librarydomain;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import java.util.List;
import java.util.Random;

public class ReviewFixture {

  private static final Random random = new Random();
  private static final List<String> REVIEW_TITLES =
      List.of("저녁시간 최고의 밥반찬", "개띵작", "최고의 책", "최악의 책", "그대 눈망울에 치얼스", "탐앤탐스 와있음");
  private static final List<String> REVIEW_CONTENTS =
      List.of("암튼 좋은 내용", "아모턴 무슨 소리인지 모르겠음", "수박주스가 달고 맛있군요", "이제 술 먹지 말아야지");

  public static Review create(Long reviewId, Member member, Book book) {
    return new Review(reviewId, member, book, createReviewTitle(), createReviewContent(), 0L);
  }

  private static String createReviewTitle() {
    return REVIEW_TITLES.get(random.nextInt(REVIEW_TITLES.size()));
  }

  private static String createReviewContent() {
    return REVIEW_CONTENTS.get(random.nextInt(REVIEW_CONTENTS.size()));
  }
}
