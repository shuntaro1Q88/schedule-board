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

/* company_holidaysテーブル作成 */
CREATE TABLE IF NOT EXISTS company_holidays(
  id SERIAL
  ,calendar_date DATE
  ,dow_index INTEGER
  ,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  ,PRIMARY KEY (calendar_date)
);
CREATE trigger update_tri BEFORE UPDATE ON company_holidays for each ROW EXECUTE PROCEDURE set_timestamp();

/* company_calendarsテーブル作成
   company_holidaysテーブル作成後に実行
   明示的に'2022-04-01'以降のカレンダーとしている */
CREATE TABLE IF NOT EXISTS company_calendars AS(
  WITH calendar_tb AS(
    SELECT
    GENERATE_SERIES('2022-04-01', CURRENT_DATE+365*10, '1 day')::DATE AS calendar_date
    ,date_part('dow', GENERATE_SERIES('2022-04-01', CURRENT_DATE+365*10, '1 day')) AS dow_index
  )
  SELECT
    calendar_tb.calendar_date
    ,CASE WHEN company_holidays.dow_index ISNULL THEN calendar_tb.dow_index
      ELSE company_holidays.dow_index END AS dow_index
  FROM calendar_tb
  LEFT JOIN company_holidays ON
    calendar_tb.calendar_date = company_holidays.calendar_date
  ORDER BY calendar_tb.calendar_date
);