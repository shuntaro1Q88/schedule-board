/* 
DB名:schedule_board 用DDL

  ファンクションset_update_timeの作成
  groupsテーブル作成
  site_usersテーブル作成
  group_memersテーブル作成
  schedule_statusesテーブル作成
  schedule_categoriesテーブル作成
  schedulesテーブル作成
  holidaysテーブル作成
  calendarsテーブル作成 

  参考記事
  https://qiita.com/kaggle_grandmaster-arai-san/items/f4fbbddb4ac646bd463b
*/

/* ファンクション"trigger_set_timestamp"の作成
   データ更新用カラム"updated_at"に現在のtimestampを入れるため */
CREATE OR REPLACE FUNCTION set_timestamp()
RETURNS TRIGGER AS $$ BEGIN NEW.updated_at = NOW(); RETURN NEW; END; $$ LANGUAGE plpgsql;

/* groupsテーブル作成 */
CREATE TABLE IF NOT EXISTS groups(
  group_id INTEGER
  ,group_name TEXT UNIQUE
  ,display_flag BOOLEAN
  ,display_order INTEGER
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,PRIMARY KEY (group_id)
);
CREATE trigger update_tri BEFORE UPDATE ON groups for each ROW EXECUTE PROCEDURE set_timestamp();

/* site_usersテーブル作成 */
CREATE TABLE IF NOT EXISTS site_users(
  id SERIAL
  ,user_id TEXT -- 社員番号
  ,group_id INTEGER
  ,family_name TEXT
  ,first_name TEXT
  ,password TEXT
  ,role TEXT
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,PRIMARY KEY (user_id)
  ,FOREIGN KEY (group_id) REFERENCES groups (group_id)
);
CREATE trigger update_tri BEFORE UPDATE ON site_users for each ROW EXECUTE PROCEDURE set_timestamp();

/* group_memersテーブル作成 */
CREATE TABLE IF NOT EXISTS group_members(
  id SERIAL
  ,member_id TEXT -- 社員番号
  ,group_id INTEGER
  ,family_name TEXT
  ,first_name TEXT
  ,display_flag BOOLEAN
  ,display_order INTEGER
  ,memo TEXT
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,PRIMARY KEY (member_id,group_id)
  ,FOREIGN KEY (group_id) REFERENCES groups (group_id)
);
CREATE trigger update_tri BEFORE UPDATE ON group_members for each ROW EXECUTE PROCEDURE set_timestamp();

/* schedule_statusesテーブル作成 */
CREATE TABLE IF NOT EXISTS schedule_statuses(
  status_id INTEGER
  ,status_symbol TEXT UNIQUE
  ,status_name TEXT UNIQUE
  ,display_flag BOOLEAN
  ,display_order INTEGER
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,PRIMARY KEY (status_id)
);
CREATE trigger update_tri BEFORE UPDATE ON schedule_statuses for each ROW EXECUTE PROCEDURE set_timestamp();

/* schedule_categoriesテーブル作成 */
CREATE TABLE IF NOT EXISTS schedule_categories(
  category_id INTEGER
  ,category_name TEXT UNIQUE
  ,category_bg_color TEXT
  ,display_flag BOOLEAN
  ,display_order INTEGER
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,PRIMARY KEY (category_id)
);
CREATE trigger update_tri BEFORE UPDATE ON schedule_categories for each ROW EXECUTE PROCEDURE set_timestamp();

/* schedulesテーブル作成 */
CREATE TABLE IF NOT EXISTS schedules(
  schedule_id SERIAL
  ,group_id INTEGER
  ,member_id TEXT
  ,subject_line TEXT
  ,main_date DATE
  ,status_id INTEGER
  ,category_id INTEGER
  ,content TEXT
  ,memo TEXT
  ,schedule_group_id TEXT
  ,add_member_id_1st TEXT
  ,add_member_id_2nd TEXT
  ,add_member_id_3rd TEXT
  ,add_member_id_4th TEXT
  ,add_member_id_5th TEXT
  ,add_date_1st DATE
  ,add_date_2nd DATE
  ,add_date_3rd DATE
  ,add_date_4th DATE
  ,add_date_5th DATE
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,created_by TEXT
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_by TEXT
  ,PRIMARY KEY (schedule_id)
  ,FOREIGN KEY (group_id, member_id) REFERENCES group_members (group_id, member_id)
  ,FOREIGN KEY (status_id) REFERENCES schedule_statuses (status_id)
  ,FOREIGN KEY (category_id) REFERENCES schedule_categories (category_id)
);
CREATE trigger update_tri BEFORE UPDATE ON schedules for each ROW EXECUTE PROCEDURE set_timestamp();

/* holidaysテーブル作成 */
CREATE TABLE IF NOT EXISTS holidays(
  id SERIAL
  ,calendar_date DATE
  ,dow_index INTEGER
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,PRIMARY KEY (calendar_date)
);
CREATE trigger update_tri BEFORE UPDATE ON holidays for each ROW EXECUTE PROCEDURE set_timestamp();

/* calendarsテーブル作成
   holidaysテーブル作成後に実行
   明示的に'2022-04-01'以降のカレンダーとしている */
CREATE TABLE IF NOT EXISTS calendars AS(
  WITH calendar_tb AS(
    SELECT
    GENERATE_SERIES('2022-04-01', CURRENT_DATE+365*10, '1 day')::DATE AS calendar_date
    ,date_part('dow', GENERATE_SERIES('2022-04-01', CURRENT_DATE+365*10, '1 day')) AS dow_index
  )
  SELECT
    calendar_tb.calendar_date
    ,CASE WHEN holidays.dow_index ISNULL THEN calendar_tb.dow_index
      ELSE holidays.dow_index END AS dow_index
  FROM calendar_tb
  LEFT JOIN holidays ON
    calendar_tb.calendar_date = holidays.calendar_date
  ORDER BY calendar_tb.calendar_date
);

INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(1,'東日本G',TRUE,1);
INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(2,'中日本G',TRUE,2);
INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(3,'西日本G',TRUE,3);
INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(51,'その他',FALSE,51);

INSERT INTO schedule_statuses(status_symbol,status_name,display_flag,display_order) VALUES('○','仮',TRUE,1);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(1,'在宅勤務','#0000ff',TRUE,1);
INSERT INTO site_users(user_id,group_id,family_name,first_name,password,role) VALUES('123123',1,'test','user','password','ADMIN');
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES('123123',1,'test','user',TRUE,1);

INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-05-02',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-05-03',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-05-04',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-05-05',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-05-06',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-08-15',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-08-16',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-08-17',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-08-18',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-08-19',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-12-29',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2022-12-30',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2023-01-02',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2023-01-03',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2023-01-04',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2023-01-05',7);
INSERT INTO holidays(calendar_date,dow_index) VALUES('2023-01-06',7);

INSERT INTO schedule_statuses(status_id,status_symbol,status_name,display_flag,display_order) VALUES(1,'★','確定',TRUE,1);
INSERT INTO schedule_statuses(status_id,status_symbol,status_name,display_flag,display_order) VALUES(2,'☆','未定',TRUE,2);

INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(1,'出張工事','#00bfff',TRUE,1);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(2,'突発工事','#ff4000',TRUE,2);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(3,'ｺｰﾙｾﾝﾀｰ対応','#ffbf00',TRUE,3);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(4,'[社外]打合せ/MTG','#7556a7',TRUE,4);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(5,'[社内]打合せ/MTG','#879bd9',TRUE,5);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(6,'有給/振休/代休','#00ff40',TRUE,6);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(7,'研修/教育','#b9faff',TRUE,7);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(8,'在宅','#b2ffd0',TRUE,8);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(9,'出社','#fcffc6',TRUE,9);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(10,'社内便当番','#f4d9e3',TRUE,10);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(11,'ゴミ当番','#c1afa8',TRUE,11);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(12,'ToDo','#ffcf7e',TRUE,12);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(13,'その他1','#b5ffce',TRUE,13);
INSERT INTO schedule_categories(category_id,category_name,category_bg_color,display_flag,display_order) VALUES(14,'その他2','#e8ffff',TRUE,14);
