package com.zerobase.socialgroupplatform.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // 공통
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
  NO_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

  // 회원 관련
  DUPLICATE_USER_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
  DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

  // 모임 관련
  NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
  NOT_FOUND_LOCATION(HttpStatus.NOT_FOUND, "존재하지 않는 지역 정보입니다."),
  NOT_FOUND_TIMESLOT(HttpStatus.NOT_FOUND, "존재하지 않는 시간대입니다."),
  INACTIVE_CATEGORY(HttpStatus.NOT_FOUND, "비활성화된 카테고리입니다."),
  INACTIVE_LOCATION(HttpStatus.NOT_FOUND, "비활성화된 지역 정보입니다."),
  INACTIVE_TIMESLOT(HttpStatus.NOT_FOUND, "비활성화된 시간대입니다."),

  NOT_FOUND_GROUP(HttpStatus.NOT_FOUND, "존재하지 않는 모임/스터디입니다."),

  // 모임 신청 관련
  ALREADY_APPLIED(HttpStatus.CONFLICT, "이미 신청했거나, 참가한 모임입니다."),
  GROUP_IS_FULL(HttpStatus.CONFLICT, "모임 인원이 가득 찼습니다."),
  GROUP_IS_PRIVATE(HttpStatus.CONFLICT, "비공개 모임입니다."),
  NOT_FOUND_APPLICATION(HttpStatus.NOT_FOUND, "신청 정보가 없습니다."),
  STATUS_IS_NOT_PENDING(HttpStatus.CONFLICT, "승인 대기 상태가 아닙니다."),
  CANNOT_CANCEL(HttpStatus.CONFLICT, "취소할 수 없는 상태입니다."),
  NOT_IN_GROUP(HttpStatus.CONFLICT, "해당 모임에 참가하고 있지 않습니다.");

  private final HttpStatus status;
  private final String message;
}
