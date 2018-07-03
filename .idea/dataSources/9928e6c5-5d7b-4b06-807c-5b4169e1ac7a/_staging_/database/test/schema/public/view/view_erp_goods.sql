CREATE VIEW view_erp_goods AS SELECT g.goods_code, g.style_name, g.goods_name_cn, g.sku, g.brand_code,
b.brand_name_cn AS brand_name, g.line_code, l.line_name_cn AS line_name, g.color_code,
c.color_name_cn AS color_name, g.size_type_code, s.type_name AS size_type_name, s.sort AS size_sort,
s.bnk_no_limit, s.bnk_less_limit, g.size_code, s.size_full_name_cn AS size_name, g.sell_year, g.season_code,
n.season_name_cn AS season_name, g.price, g.face, g.material, g.filler, g.comment, g.update_time,
g.insert_time FROM ((((((erp_goods g
  LEFT JOIN erp_goods_type p ON (((g.type_code)::text = (p.type_code)::text)))
  LEFT JOIN erp_brand b ON (((g.brand_code)::text = (b.brand_code)::text)))
  LEFT JOIN erp_produce_line l ON (((g.line_code)::text = (l.line_code)::text)))
  LEFT JOIN erp_goods_color c ON (((g.color_code)::text = (c.color_code)::text)))
  LEFT JOIN erp_goods_size s ON ((((g.size_type_code)::text = (s.size_type_code)::text)
                                  AND ((g.size_code)::text = (s.size_code)::text))))
  LEFT JOIN erp_goods_season n ON (((g.season_code)::text = (n.season_code)::text)))
                              ORDER BY g.goods_code, g.color_code, s.sort;
