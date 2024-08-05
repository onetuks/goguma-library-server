package com.onetuks.libraryapi.attendance.controller;

import com.onetuks.libraryapi.attendance.dto.response.AttendanceResponse;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.attendance.service.AttendanceService;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/attendances")
public class AttendanceRestController {

  private static final Logger log = LoggerFactory.getLogger(AttendanceRestController.class);

  private final AttendanceService attendanceService;

  public AttendanceRestController(AttendanceService attendanceService) {
    this.attendanceService = attendanceService;
  }

  /**
   * 출석 정보를 저장한다.
   *
   * <p>오늘 날짜만 출석 정보로 저장
   *
   * <p>날짜 형식 : yyyy-MM-dd (ISO.DATE 형식)
   *
   * @param loginId : 로그인 ID
   * @param date : 출석 날짜
   * @return : 출석 정보
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AttendanceResponse> postNewAttendance(
      @LoginId Long loginId,
      @RequestParam(name = "date") @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    Attendance result = attendanceService.register(loginId, date);
    AttendanceResponse response = AttendanceResponse.from(result);

    log.info("[출석] 출석 등록 - memberId: {}, date: {}", loginId, date);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
