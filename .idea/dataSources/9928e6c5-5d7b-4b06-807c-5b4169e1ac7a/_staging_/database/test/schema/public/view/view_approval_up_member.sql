  CREATE VIEW view_approval_up_member AS
  SELECT
  history.member_id, history.all_point, history.max_point, master.member_name,
  master.member_level_id AS current_level_id, mll.member_level_code AS approval_level_id
  FROM (((SELECT mmb_point_record.member_id, sum(mmb_point_record.point) AS all_point, max(mmb_point_record.point)
  AS max_point FROM mmb_point_record WHERE (((date_part('year'::text, mmb_point_record.point_time) = date_part('year'::text, now()))
  AND ((mmb_point_record.rule_id)::text = '10'::text)) AND ((mmb_point_record.deleted)::text = '0'::text))
  GROUP BY mmb_point_record.member_id) history JOIN mmb_master master ON (((((master.member_id)::text = (history.member_id)::text)
  AND ((master.deleted)::text = '0'::text)) AND((master.is_valid)::text = '0'::text))))
  JOIN mmb_level_rule mll ON ((((mll.point_lower <= history.all_point) AND (history.all_point <= mll.point_upper))
  OR ((history.max_point >= mll.point_single) AND ((master.member_level_id)::text < (mll.member_level_code)::text)))));
