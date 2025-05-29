-- create schema `millie`;
use `millie`;

-- 편의를 위해 product (상품) 테이블은 별도로 생성하지 않습니다.

CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(50) NOT NULL COMMENT '관리자 아이디',
    password VARCHAR(300) NOT NULL COMMENT '관리자 비밀번호',
    name VARCHAR(300) NOT NULL COMMENT '관리자명',
    created_date DATETIME NOT NULL DEFAULT NOW() COMMENT '생성 일시'
);

CREATE TABLE seller (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(300) NOT NULL COMMENT '판매자명',
    created_date DATETIME NOT NULL DEFAULT NOW() COMMENT '생성 일시'
);

CREATE TABLE `order` (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	order_no VARCHAR(50) NOT NULL COMMENT '주문번호',
	name VARCHAR(300) NULL COMMENT '주문명',
    created_date DATETIME NOT NULL DEFAULT NOW() COMMENT '생성 일시',
    is_settled BIT NOT NULL DEFAULT 0 COMMENT '정산 완료 여부'
);

CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '주문 ID',
    product_id BIGINT NOT NULL COMMENT '상품 ID',
    product_name VARCHAR(300) NOT NULL COMMENT '상품명',
    ea INT NOT NULL COMMENT '수량',
    commission_rate INT NOT NULL COMMENT '수수료율',
    seller_id BIGINT NOT NULL COMMENT '판매자 ID',
    sell_price BIGINT NOT NULL COMMENT '판매가',
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE settlement_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL COMMENT '판매자 ID',
    status_type VARCHAR(50) NOT NULL COMMENT '정산 요청 상태',
    standard_start_date DATETIME NOT NULL COMMENT '정산 기준 시작일',
    standard_end_date DATETIME NOT NULL COMMENT '정산 기준 종료일',
    request_date DATETIME NOT NULL DEFAULT NOW() COMMENT '정산 요청 일시',
    CONSTRAINT fk_settlement_request_seller FOREIGN KEY (seller_id) REFERENCES seller(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '정산 요청';

CREATE TABLE settlement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    settlement_request_id BIGINT NOT NULL COMMENT '정산 요청 ID',
    total_amount BIGINT NOT NULL COMMENT '정산 총액',
    created_date DATETIME NOT NULL DEFAULT NOW() COMMENT '생성 일시',
    CONSTRAINT fk_settlement_settlement_request FOREIGN KEY (settlement_request_id) REFERENCES settlement_request(id) ON DELETE CASCADE ON UPDATE CASCADE
);
