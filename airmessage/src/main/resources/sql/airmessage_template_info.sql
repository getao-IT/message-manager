/*
 Navicat Premium Data Transfer

 Source Server         : geodl_iecas
 Source Server Type    : PostgreSQL
 Source Server Version : 120003
 Source Host           : 192.168.9.64:32189
 Source Catalog        : geodl_iecas
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120003
 File Encoding         : 65001

 Date: 24/03/2022 17:44:49
*/

DROP SEQUENCE IF EXISTS "public".airmessage_template_info_id_seq;
CREATE SEQUENCE airmessage_template_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for airmessage_template_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."airmessage_template_info";
CREATE TABLE "public"."airmessage_template_info" (
  "id" int4 NOT NULL DEFAULT nextval('airmessage_template_info_id_seq'::regclass),
  "service_id" int4 NOT NULL,
  "source_user_id" int4 NOT NULL,
  "notification_level" int4 NOT NULL,
  "notification_type" varchar(255) COLLATE "pg_catalog"."default",
  "notification_title" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "target_user_level" varchar(255) COLLATE "pg_catalog"."default",
  "target_user_id" int4,
  "create_time" timestamp(6),
  "invalidated_time" timestamp(6) NOT NULL,
  "notification_content" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "proclamation" bool
)
;
COMMENT ON COLUMN "public"."airmessage_template_info"."id" IS '通知消息ID';
COMMENT ON COLUMN "public"."airmessage_template_info"."service_id" IS '服务ID';
COMMENT ON COLUMN "public"."airmessage_template_info"."source_user_id" IS '创建消息用户ID';
COMMENT ON COLUMN "public"."airmessage_template_info"."notification_level" IS '通知等级';
COMMENT ON COLUMN "public"."airmessage_template_info"."notification_type" IS '通知类型';
COMMENT ON COLUMN "public"."airmessage_template_info"."notification_title" IS '通知标题';
COMMENT ON COLUMN "public"."airmessage_template_info"."target_user_level" IS '通知目标用户等级';
COMMENT ON COLUMN "public"."airmessage_template_info"."target_user_id" IS '通知目标用户ID';
COMMENT ON COLUMN "public"."airmessage_template_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."airmessage_template_info"."invalidated_time" IS '失效时间';
COMMENT ON COLUMN "public"."airmessage_template_info"."notification_content" IS '通知内容';
COMMENT ON COLUMN "public"."airmessage_template_info"."proclamation" IS '是否创建公告';

-- ----------------------------
-- Primary Key structure for table airmessage_template_info
-- ----------------------------
ALTER TABLE "public"."airmessage_template_info" ADD CONSTRAINT "airmessage_template_info_pkey" PRIMARY KEY ("id");
