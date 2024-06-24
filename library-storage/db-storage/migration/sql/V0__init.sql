CREATE TABLE IF NOT EXISTS member_statics
(
    member_statics_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '멤버 통계 식별자',
    review_counts BIGINT NOT NULL DEFAULT 0 COMMENT '서평 수',
    follower_counts BIGINT NOT NULL DEFAULT 0 COMMENT '팔로워 수',
    following_counts BIGINT NOT NULL DEFAULT 0 COMMENT '팔로잉 수',
    review_category_counts JSON COMMENT '리뷰 카테고리 별 서평수',
    PRIMARY KEY (member_statics_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='멤버 통계 테이블';

CREATE TABLE IF NOT EXISTS members
(
    member_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '멤버 식별자',
    social_id VARCHAR(255) NOT NULL COMMENT '소셜 식별자',
    client_provider VARCHAR(50) NOT NULL COMMENT '클라이언트 제공자',
    roles JSON NOT NULL COMMENT '멤버 권한',
    nickname VARCHAR(50) COMMENT '멤버 닉네임',
    introduction VARCHAR(100) COMMENT '멤버 소개',
    interested_categories JSON COMMENT '멤버 관심 카테고리',
    points BIGINT NOT NULL DEFAULT 0 COMMENT '멤버 포인트',
    profile_img_uri VARCHAR(255) NOT NULL DEFAULT '/profile/default-profile.png' COMMENT '멤버 프로필 이미지 URI',
    is_alarm_accepted BOOLEAN NOT NULL DEFAULT TRUE COMMENT '알림 수신 여부',
    member_statics_id BIGINT NOT NULL COMMENT '멤버 통계 식별자',
    PRIMARY KEY (member_id),
    UNIQUE KEY unq_social_id_client_provider (social_id, client_provider),
    UNIQUE KEY unq_nickname (nickname),
    FOREIGN KEY (member_statics_id) REFERENCES member_statics (member_statics_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='멤버 테이블';

CREATE TABLE IF NOT EXISTS follows
(
    follow_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '팔로우 식별자',
    follower_id BIGINT NOT NULL COMMENT '나를 팔로우한 멤버(팔로워) 식별자',
    followee_id BIGINT NOT NULL COMMENT '내가 팔로우한 멤버(팔로이) 식별자',
    PRIMARY KEY (follow_id),
    FOREIGN KEY (follower_id) REFERENCES members (member_id),
    FOREIGN KEY (followee_id) REFERENCES members (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='팔로우 테이블';

CREATE TABLE IF NOT EXISTS badges
(
    badge_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '뱃지 식별자',
    name VARCHAR(255) NOT NULL COMMENT '뱃지 이름',
    detail VARCHAR(255) NOT NULL COMMENT '뱃지 설명',
    badge_icon_uri VARCHAR(255) NOT NULL COMMENT '뱃지 아이콘 URI',
    PRIMARY KEY (badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='뱃지 테이블';

CREATE TABLE IF NOT EXISTS member_badges
(
    member_badge_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '멤버 뱃지 식별자',
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    badge_id BIGINT NOT NULL COMMENT '뱃지 식별자',
    PRIMARY KEY (member_badge_id),
    FOREIGN KEY (member_id) REFERENCES members (member_id),
    FOREIGN KEY (badge_id) REFERENCES badges (badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='멤버 뱃지 테이블';

CREATE TABLE IF NOT EXISTS books
(
    book_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '책 식별자',
    title VARCHAR(255) NOT NULL COMMENT '책 제목',
    author_name VARCHAR(50) NOT NULL COMMENT '저자 이름',
    introduction VARCHAR(5000) COMMENT '책 소개',
    isbn VARCHAR(50) COMMENT '국제 표준 도서 번호',
    publisher VARCHAR(255) COMMENT '출판사',
    category VARCHAR(50) COMMENT '카테고리',
    cover_img_uri VARCHAR(255) COMMENT '책 표지 이미지 URI',
    is_indie BOOLEAN NOT NULL DEFAULT FALSE COMMENT '독립출판물 여부',
    is_permitted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '서비스 제공 허가 여부',
    PRIMARY KEY (book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='책 테이블';

CREATE TABLE IF NOT EXISTS reviews
(
    review_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '서평 식별자',
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    book_id BIGINT NOT NULL COMMENT '책 식별자',
    content VARCHAR(5000) NOT NULL COMMENT '서평 내용',
    like_counts BIGINT NOT NULL DEFAULT 0 COMMENT '서평 좋아요 수',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '서평 생성일',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '서평 수정일',
    PRIMARY KEY (review_id),
    FOREIGN KEY (member_id) REFERENCES members (member_id),
    FOREIGN KEY (book_id) REFERENCES books (book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='서평 테이블';

CREATE TABLE IF NOT EXISTS favorites
(
    favorite_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '즐겨찾기 식별자',
    member_id BIGINT NOT NULL COMMENT '멤버 식별자',
    review_id BIGINT NOT NULL COMMENT '서평 식별자',
    PRIMARY KEY (favorite_id),
    FOREIGN KEY (member_id) REFERENCES members (member_id),
    FOREIGN KEY (review_id) REFERENCES reviews (review_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='즐겨찾기 테이블';
