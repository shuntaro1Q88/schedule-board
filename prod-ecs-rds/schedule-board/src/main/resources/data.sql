-- groupsテーブルに初期値をインサート
INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(1,'東日本G',TRUE,1);
INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(2,'中日本G',TRUE,2);
INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(3,'西日本G',TRUE,3);
INSERT INTO groups(group_id,group_name,display_flag,display_order) VALUES(51,'その他',FALSE,51);

-- site_usersテーブルに初期値をインサート
INSERT INTO site_users(user_id,group_id,family_name,first_name, password, role) 
VALUES(999999,2,'TEST','USER','$2a$10$ciMCnjMI65YQT3EwbfY4keysBsOuCTOFPRmjZqCsacmXOOLRpKsNO','admin');

-- group_membersテーブルに初期値をインサート
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900101,1,'古田','夕子',true,101);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900102,1,'酒井','記代',true,102);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900103,1,'久保','虎之助',true,103);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900104,1,'菊池','謹二',true,104);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900105,1,'高木','美智',true,105);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900106,1,'大城','俊郎',true,106);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900107,1,'内海','真志',true,107);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900108,1,'後藤','火呂絵',true,108);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900109,1,'竹村','育雄',true,109);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900110,1,'吉本','文之',true,110);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900201,2,'堀内','望',true,201);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900202,2,'福井','秋徳',true,202);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900203,2,'田上','理沙',true,203);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900204,2,'中西','亮子',true,204);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900205,2,'三木','孝成',true,205);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900206,2,'川島','早葉子',true,206);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900207,2,'西野','康三',true,207);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900208,2,'河野','和広',true,208);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900209,2,'西山','豊司',true,209);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900210,2,'新井','君吉',true,210);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900301,3,'西田','有志',true,301);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900302,3,'小野寺','太志',true,302);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900303,3,'深津','旭弘',true,303);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900304,3,'大竹','壱青',true,304);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900305,3,'大塚','達宣',true,305);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900306,3,'山内','晶大',true,306);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900307,3,'高梨','健太',true,307);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900308,3,'関田','誠大',true,308);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900309,3,'大宅','真樹',true,309);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900310,3,'富田','将馬',true,310);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900311,3,'髙橋','藍',true,311);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900312,3,'小川','智大',true,312);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900313,3,'李','博',true,313);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900314,3,'宮浦','健人',true,314);
INSERT INTO group_members(member_id,group_id,family_name,first_name,display_flag,display_order) VALUES(900315,3,'山崎','彰都',true,315);

-- schedule_statusesテーブルに初期値をインサート
INSERT INTO schedule_statuses(status_id,status_symbol,status_name,display_flag,display_order) VALUES(0,'','未設定',TRUE,0);
INSERT INTO schedule_statuses(status_id,status_symbol,status_name,display_flag,display_order) VALUES(1,'★','確定',TRUE,1);
INSERT INTO schedule_statuses(status_id,status_symbol,status_name,display_flag,display_order) VALUES(2,'☆','未定',TRUE,2);

-- schedule_categoriesテーブルに初期値をインサート
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