SELECT c.id, c.name, c.image, c.price, c.type_id AS "typeId", c.style, c.description, c.stock, c.create_time AS "createTime", c.update_time AS "updateTime", t.type_name AS "typeName"
FROM t_clothes c LEFT JOIN t_type t ON c.type_id = t.id
ORDER BY c.create_time DESC
LIMIT 1;
