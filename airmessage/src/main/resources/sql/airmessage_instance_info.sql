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

 Date: 24/03/2022 17:44:43
*/

DROP SEQUENCE IF EXISTS "public".airmessage_instance_info_id_seq;
CREATE SEQUENCE airmessage_instance_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for airmessage_instance_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."airmessage_instance_info";
CREATE TABLE "public"."airmessage_instance_info" (
  "id" int4 NOT NULL DEFAULT nextval('airmessage_instance_info_id_seq'::regclass),
  "user_id" int4 NOT NULL,
  "message_id" int4 NOT NULL,
  "pull_time" timestamp(6) NOT NULL,
  "read" bool NOT NULL,
  "deleted" bool NOT NULL
)
;
COMMENT ON COLUMN "public"."airmessage_instance_info"."id" IS '实例ID';
COMMENT ON COLUMN "public"."airmessage_instance_info"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."airmessage_instance_info"."message_id" IS '通知消息ID';
COMMENT ON COLUMN "public"."airmessage_instance_info"."pull_time" IS '消息拉取时间';
COMMENT ON COLUMN "public"."airmessage_instance_info"."read" IS '是否已读';
COMMENT ON COLUMN "public"."airmessage_instance_info"."deleted" IS '是否删除';

-- ----------------------------
-- Primary Key structure for table airmessage_instance_info
-- ----------------------------
ALTER TABLE "public"."airmessage_instance_info" ADD CONSTRAINT "airmessage_instance_info_pkey" PRIMARY KEY ("id");
