package com.onetuks.dbstorage.common;

import com.onetuks.dbstorage.attendance.repository.AttendanceEntityJpaRepository;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.repository.BookEntityJpaRepository;
import com.onetuks.dbstorage.book.repository.BookPickEntityJpaRepository;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.member.repository.FollowEntityJpaRepository;
import com.onetuks.dbstorage.member.repository.MemberEntityJpaRepository;
import com.onetuks.dbstorage.review.entity.ReviewEntity;
import com.onetuks.dbstorage.review.repository.ReviewEntityJpaRepository;
import com.onetuks.dbstorage.review.repository.ReviewPickEntityJpaRepository;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import org.springframework.stereotype.Repository;

@Repository
public class DeletionRepository {

  private final MemberEntityJpaRepository memberEntityJpaRepository;
  private final BookEntityJpaRepository bookEntityJpaRepository;
  private final BookPickEntityJpaRepository bookPickEntityJpaRepository;
  private final ReviewEntityJpaRepository reviewEntityJpaRepository;
  private final ReviewPickEntityJpaRepository reviewPickEntityJpaRepository;
  private final FollowEntityJpaRepository followEntityJpaRepository;
  private final AttendanceEntityJpaRepository attendanceEntityJpaRepository;

  public DeletionRepository(
      MemberEntityJpaRepository memberEntityJpaRepository,
      BookEntityJpaRepository bookEntityJpaRepository,
      BookPickEntityJpaRepository bookPickEntityJpaRepository,
      ReviewEntityJpaRepository reviewEntityJpaRepository,
      ReviewPickEntityJpaRepository reviewPickEntityJpaRepository,
      FollowEntityJpaRepository followEntityJpaRepository,
      AttendanceEntityJpaRepository attendanceEntityJpaRepository) {
    this.memberEntityJpaRepository = memberEntityJpaRepository;
    this.bookEntityJpaRepository = bookEntityJpaRepository;
    this.bookPickEntityJpaRepository = bookPickEntityJpaRepository;
    this.reviewEntityJpaRepository = reviewEntityJpaRepository;
    this.reviewPickEntityJpaRepository = reviewPickEntityJpaRepository;
    this.followEntityJpaRepository = followEntityJpaRepository;
    this.attendanceEntityJpaRepository = attendanceEntityJpaRepository;
  }

  public void deleteMember(long memberId) {
    MemberEntity memberEntity =
        memberEntityJpaRepository
            .findById(memberId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 멤버입니다."));

    attendanceEntityJpaRepository.deleteAllByMemberEntity(memberEntity);
    followEntityJpaRepository.deleteAllByFollower(memberEntity);
    followEntityJpaRepository.deleteAllByFollowee(memberEntity);
    reviewPickEntityJpaRepository.deleteAllByMemberEntity(memberEntity);
    reviewEntityJpaRepository.deleteAllByMemberEntity(memberEntity);
    bookPickEntityJpaRepository.deleteAllByMemberEntity(memberEntity);
    bookEntityJpaRepository.deleteAllByMemberEntity(memberEntity);

    memberEntityJpaRepository.delete(memberEntity);
  }

  public void deleteBook(long bookId) {
    BookEntity bookEntity =
        bookEntityJpaRepository
            .findById(bookId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 책입니다."));

    reviewPickEntityJpaRepository.deleteAllByReviewEntityBookEntity(bookEntity);
    reviewEntityJpaRepository.deleteAllByBookEntity(bookEntity);
    bookPickEntityJpaRepository.deleteAllByBookEntity(bookEntity);

    bookEntityJpaRepository.delete(bookEntity);
  }

  public void deleteReview(long reviewId) {
    ReviewEntity reviewEntity =
        reviewEntityJpaRepository
            .findById(reviewId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 리뷰입니다."));

    reviewPickEntityJpaRepository.deleteAllByReviewEntity(reviewEntity);

    reviewEntityJpaRepository.delete(reviewEntity);
  }
}
