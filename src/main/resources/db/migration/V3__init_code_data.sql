-- 성별 코드 그룹
INSERT INTO code_group (code_group, name, create_id) VALUES ('001', '성별', 1);

-- 성별 코드
INSERT INTO code (code_group_id, code, name, info, create_id) VALUES (1, '001', '남', '남자', 1);
INSERT INTO code (code_group_id, code, name, info, create_id) VALUES (1, '002', '여', '여자', 1);
