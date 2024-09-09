CREATE TABLE IF NOT EXISTS member_statics
(
    member_statics_id      BIGINT NOT NULL AUTO_INCREMENT COMMENT '멤버 통계 식별자',
    review_counts          BIGINT NOT NULL DEFAULT 0 COMMENT '서평 수',
    follower_counts        BIGINT NOT NULL DEFAULT 0 COMMENT '팔로워 수',
    following_counts       BIGINT NOT NULL DEFAULT 0 COMMENT '팔로잉 수',
    review_category_counts JSON COMMENT '리뷰 카테고리 별 서평수',
    PRIMARY KEY (member_statics_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='멤버 통계 테이블';

CREATE TABLE IF NOT EXISTS members
(
    member_id                    BIGINT       NOT NULL AUTO_INCREMENT COMMENT '멤버 식별자',
    social_id                    VARCHAR(255) NOT NULL COMMENT '소셜 식별자',
    client_provider              VARCHAR(50)  NOT NULL COMMENT '클라이언트 제공자',
    roles                        JSON         NOT NULL COMMENT '멤버 권한',
    nickname                     VARCHAR(50)  NOT NULL COMMENT '멤버 닉네임', -- 10자
    introduction                 VARCHAR(200) COMMENT '멤버 소개',           -- 50자
    instagram_url                VARCHAR(255) COMMENT '멤버 인스타그램 URL',
    interested_categories        JSON         NOT NULL COMMENT '멤버 관심 카테고리',
    is_alarm_accepted            BOOLEAN      NOT NULL DEFAULT TRUE COMMENT '알림 수신 여부',
    points                       BIGINT       NOT NULL DEFAULT 0 COMMENT '멤버 포인트',
    badges                       JSON         NOT NULL COMMENT '멤버 뱃지 목록',
    profile_image_uri            VARCHAR(255) NOT NULL DEFAULT 'default-profile.png' COMMENT '멤버 프로필 이미지 URI',
    profile_background_image_uri VARCHAR(255) NOT NULL DEFAULT 'default-profile-background.png' COMMENT '멤버 프로필 배경 이미지 URI',
    member_statics_id            BIGINT       NOT NULL COMMENT '멤버 통계 식별자',
    PRIMARY KEY (member_id),
    UNIQUE KEY unq_social_id_client_provider (social_id, client_provider),
    UNIQUE KEY unq_nickname (nickname),
    FOREIGN KEY fk_member_member_statics_id (member_statics_id) REFERENCES member_statics (member_statics_id) ON
        DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='멤버 테이블';

CREATE TABLE IF NOT EXISTS point_histories
(
    point_history_id BIGINT      NOT NULL AUTO_INCREMENT COMMENT '포인트 항목 식별자',
    member_id        BIGINT      NOT NULL COMMENT '멤버 식별자',
    activity         VARCHAR(50) NOT NULL COMMENT '포인트 내역 활동',
    points           BIGINT      NOT NULL DEFAULT 0 COMMENT '포인트',
    created_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '포인트 내역일',
    PRIMARY KEY (point_history_id),
    FOREIGN KEY fk_point_histories_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='포인트 내역 테이블';

CREATE TABLE IF NOT EXISTS books
(
    book_id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '책 식별자',
    member_id       BIGINT       NOT NULL COMMENT '멤버 식별자',
    title           VARCHAR(255) NOT NULL COMMENT '책 제목',
    author_name     VARCHAR(100) NOT NULL COMMENT '저자 이름',
    introduction    TEXT COMMENT '책 소개',
    isbn            VARCHAR(50) COMMENT '국제 표준 도서 번호', -- 13자
    publisher       VARCHAR(255) COMMENT '출판사',
    categories      JSON         NOT NULL COMMENT '카테고리',
    cover_image_uri VARCHAR(255) NOT NULL DEFAULT 'default-cover.png' COMMENT '책 표지 이미지 URI',
    is_indie        BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '독립출판물 여부',
    is_permitted    BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '서비스 제공 허가 여부',
    pick_counts     BIGINT       NOT NULL DEFAULT 0 COMMENT '도서픽 카운트',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '도서 등록일',
    PRIMARY KEY (book_id),
    FOREIGN KEY fk_books_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE,
    UNIQUE KEY unq_isbn (isbn)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='책 테이블';

CREATE TABLE IF NOT EXISTS reviews
(
    review_id      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '서평 식별자',
    member_id      BIGINT        NOT NULL COMMENT '멤버 식별자',
    book_id        BIGINT        NOT NULL COMMENT '책 식별자',
    review_title   VARCHAR(255)  NOT NULL COMMENT '서평 제목',
    review_content VARCHAR(5000) NOT NULL COMMENT '서평 내용',
    pick_counts    BIGINT        NOT NULL DEFAULT 0 COMMENT '서평픽 카운트',
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '서평 생성일',
    updated_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '서평 수정일',
    PRIMARY KEY (review_id),
    FOREIGN KEY fk_reviews_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE,
    FOREIGN KEY fk_review_book_id (book_id) REFERENCES books (book_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='서평 테이블';

CREATE TABLE IF NOT EXISTS review_picks
(
    review_pick_id BIGINT   NOT NULL AUTO_INCREMENT COMMENT '서평픽 식별자',
    member_id      BIGINT   NOT NULL COMMENT '멤버 식별자',
    review_id      BIGINT   NOT NULL COMMENT '서평 식별자',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '서평픽 생성일',
    PRIMARY KEY (review_pick_id),
    FOREIGN KEY fk_review_picks_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE,
    FOREIGN KEY fk_review_picks_review_id (review_id) REFERENCES reviews (review_id) ON DELETE CASCADE,
    UNIQUE KEY unq_member_id_review_id (member_id, review_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='서평픽 테이블';

CREATE TABLE IF NOT EXISTS book_picks
(
    book_pick_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '북픽 식별자',
    member_id    BIGINT NOT NULL COMMENT '멤버 식별자',
    book_id      BIGINT NOT NULL COMMENT '책 식별자',
    PRIMARY KEY (book_pick_id),
    FOREIGN KEY fk_book_picks_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE,
    FOREIGN KEY fk_book_picks_book_id (book_id) REFERENCES books (book_id) ON DELETE CASCADE,
    UNIQUE KEY unq_member_id_book_id (member_id, book_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='북픽 테이블';

CREATE TABLE IF NOT EXISTS follows
(
    follow_id   BIGINT NOT NULL AUTO_INCREMENT COMMENT '팔로우 식별자',
    follower_id BIGINT NOT NULL COMMENT '팔로우한 멤버(팔로워) 식별자',
    followee_id BIGINT NOT NULL COMMENT '팔로우 받은 멤버(팔로이) 식별자',
    PRIMARY KEY (follow_id),
    FOREIGN KEY fk_follows_follower_id (follower_id) REFERENCES members (member_id) ON DELETE CASCADE,
    FOREIGN KEY fk_follows_followee_id (followee_id) REFERENCES members (member_id) ON DELETE CASCADE,
    UNIQUE KEY unq_follower_id_followee_id (follower_id, followee_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='팔로우 테이블';

CREATE TABLE IF NOT EXISTS weekly_featured_books_events
(
    weekly_featured_books_event_id BIGINT   NOT NULL AUTO_INCREMENT COMMENT '금주도서 이벤트 식별자',
    started_at                     DATETIME NOT NULL COMMENT '이벤트 시작일',
    ended_at                       DATETIME NOT NULL COMMENT '이벤트 종료일',
    PRIMARY KEY (weekly_featured_books_event_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='금주도서 이벤트 테이블';

CREATE TABLE IF NOT EXISTS weekly_featured_books
(
    weekly_featured_book_id        BIGINT NOT NULL AUTO_INCREMENT COMMENT '금주도서 식별자',
    weekly_featured_books_event_id BIGINT NOT NULL COMMENT '금주도서 이벤트 식별자',
    book_id                        BIGINT NOT NULL COMMENT '도서 식별자',
    PRIMARY KEY (weekly_featured_book_id),
    FOREIGN KEY fk_weekly_featured_books_event_id (weekly_featured_books_event_id)
        REFERENCES weekly_featured_books_events (weekly_featured_books_event_id)
        ON DELETE CASCADE,
    FOREIGN KEY fk_weekly_featured_books_book_id (book_id) REFERENCES books (book_id) ON DELETE CASCADE,
    UNIQUE KEY unq_book_id (book_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='금주도서 테이블';

CREATE TABLE IF NOT EXISTS attendances
(
    attendance_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '출석 식별자',
    member_id     BIGINT NOT NULL COMMENT '멤버 식별자',
    attended_at   DATE   NOT NULL DEFAULT (CURRENT_DATE) COMMENT '출석 일시',
    PRIMARY KEY (attendance_id),
    FOREIGN KEY fk_attendances_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE,
    UNIQUE KEY unq_member_id_attended_at (member_id, attended_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='출석 테이블';

CREATE TABLE IF NOT EXISTS notifications
(
    notification_id   BIGINT                                    NOT NULL AUTO_INCREMENT COMMENT '알림 식별자',
    member_id         BIGINT                                    NOT NULL COMMENT '멤버 식별자(발신자)',
    message           VARCHAR(255)                              NOT NULL COMMENT '알림 메시지',
    notification_type ENUM ('NOTICE', 'EVENT', 'SOCIAL_UPDATE') NOT NULL COMMENT '알림 타입',
    created_at        DATETIME                                  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '알림 생성일',
    PRIMARY KEY (notification_id),
    FOREIGN KEY fk_notifications_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='알림 테이블';

CREATE TABLE notification_receipts
(
    notification_receipts_id BIGINT AUTO_INCREMENT COMMENT '알림 수신 식별자',
    member_id                BIGINT   NOT NULL COMMENT '멤버 식별자(수신자)',
    notification_id          BIGINT   NOT NULL COMMENT '알림 식별자',
    is_read                  BOOLEAN  NOT NULL DEFAULT FALSE COMMENT '읽음 상태',
    read_at                  DATETIME NULL COMMENT '읽은 시간',
    PRIMARY KEY (notification_receipts_id),
    FOREIGN KEY fk_notification_receipts_member_id (member_id) REFERENCES members (member_id) ON DELETE CASCADE,
    FOREIGN KEY fk_notification_receipts_notification_id (notification_id) REFERENCES notifications (notification_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='알림 수신 테이블';
