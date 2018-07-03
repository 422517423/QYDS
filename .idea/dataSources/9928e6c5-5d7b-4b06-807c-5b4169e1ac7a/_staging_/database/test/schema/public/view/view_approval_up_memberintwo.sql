CREATE VIEW view_approval_up_memberintwo AS
  SELECT ord_master.member_id, mmb_master.member_name, mmb_master.telephone,
    (sum(erp_order_master.amount) + COALESCE((SELECT sum(ord_master.pay_infact)
      AS sum FROM ord_master WHERE ((((ord_master.member_id)::text =
  ((SELECT mmb_master.member_id FROM mmb_master WHERE ((mmb_master.telephone)::text
 = (erp_order_master.member_code)::text)))::text) AND (date_part('year'::text, ord_master.pay_time) =
 (date_part('year'::text, now()) - (1)::double precision))) OR
        (date_part('year'::text, ord_master.pay_time) = date_part('year'::text, now())))),
    (0)::numeric)) AS all_point, CASE WHEN (max(ord_master.pay_infact) > max(erp_order_master.amount))
    THEN max(ord_master.pay_infact) ELSE max(erp_order_master.amount) END
    AS max_point, mmb_master.member_level_id AS current_level_id,
    (SELECT mmb_level_rule.member_level_code FROM mmb_level_rule WHERE
      ((mmb_level_rule.member_level_id)::text = '4ef890a4-e107-4df0-8114-c58cd7f29d73'::text))
      AS approval_level_id FROM ((ord_master JOIN erp_order_master ON (((erp_order_master.member_code)
     ::text = (ord_master.delivery_phone)::text))) JOIN mmb_master ON
(((mmb_master.member_id)::text = (ord_master.member_id)::text))) GROUP BY ord_master.member_id, erp_order_master.member_code, mmb_master.member_name, mmb_master.telephone, mmb_master.member_level_id HAVING (((mmb_master.member_level_id)::text = '10'::text) AND ((CASE WHEN (max(ord_master.pay_infact) > max(erp_order_master.amount)) THEN max(ord_master.pay_infact) ELSE max(erp_order_master.amount) END > ((SELECT mmb_level_rule.point_single FROM mmb_level_rule WHERE ((mmb_level_rule.member_level_code)::text = '30'::text)))::numeric) OR ((sum(erp_order_master.amount) + COALESCE((SELECT sum(ord_master.pay_infact) AS sum FROM ord_master WHERE ((((ord_master.member_id)::text = ((SELECT mmb_master.member_id FROM mmb_master WHERE ((mmb_master.telephone)::text = (erp_order_master.member_code)::text)))::text) AND (date_part('year'::text, ord_master.pay_time) = (date_part('year'::text, now()) - (1)::double precision))) OR (date_part('year'::text, ord_master.pay_time) = date_part('year'::text, now())))), (0)::numeric)) > ((SELECT mmb_level_rule.point_cumulative FROM mmb_level_rule WHERE ((mmb_level_rule.member_level_code)::text = '30'::text)))::numeric)));
