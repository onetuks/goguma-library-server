package com.onetuks.librarydomain.attendance.service;

import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.attendance.repository.AttendanceRepository;
import com.onetuks.librarydomain.global.point.producer.PointEventProducer;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AttendanceService {

  private final AttendanceRepository attendanceRepository;
  private final MemberRepository memberRepository;

  private final PointEventProducer pointEventProducer;

  public AttendanceService(
      AttendanceRepository attendanceRepository,
      MemberRepository memberRepository,
      PointEventProducer pointEventProducer) {
    this.attendanceRepository = attendanceRepository;
    this.memberRepository = memberRepository;
    this.pointEventProducer = pointEventProducer;
  }

  @Transactional
  public Attendance register(long loginId, LocalDate date) {
    if (!date.isEqual(LocalDate.now())) {
      throw new IllegalArgumentException("오늘 날짜만 출석 체크가 가능합니다.");
    }

    int attendedCount = attendanceRepository.readThisMonth(loginId);

    Member member =
        memberRepository.update(
            memberRepository.read(loginId).creditBadgeForAttendance(attendedCount + 1));

    Attendance attendance = attendanceRepository.create(new Attendance(null, member, null));

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            pointEventProducer.creditPointForAttendance(loginId, attendedCount + 1);
          }
        }
    );

    return attendance;
  }

  @Transactional(readOnly = true)
  public List<Attendance> searchAllThisMonthAttendances(long loginId, LocalDate date) {
    YearMonth yearMonth = YearMonth.from(date);
    LocalDate startOfMonth = yearMonth.atDay(1);
    LocalDate endOfMonth = yearMonth.atEndOfMonth();

    return attendanceRepository.readAllThisMonth(loginId, startOfMonth, endOfMonth);
  }
}
