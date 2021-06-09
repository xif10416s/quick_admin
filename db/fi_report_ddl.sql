-- ----------------------------
-- 金融业科技信息综合管理平台
-- 数据上报接入模块
-- ----------------------------
-- ----------------------------
-- 元数据信息表 金融基础设施 financial infrastructure
-- ----------------------------
DROP TABLE IF EXISTS `fi_metadata`;
CREATE TABLE `fi_metadata`  (
  `id` int  auto_increment  primary key COMMENT '主键id',
  `data_type` varchar(50)  NOT NULL COMMENT '采集接口标识符',
  `data_type_name` varchar(50)  NOT NULL COMMENT '采集接口名称',
  `facilityDescriptor`  varchar(32)   NOT NULL COMMENT '全局唯一标识符',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态：0 未同步，1 同步中 ， 2 同步失败 ， 3  已上传， ',
  `json` TEXT  COMMENT '元数据信息',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  INDEX `idx_data_type`(`data_type`) USING BTREE,
  INDEX `idx_facilityDescriptor`(`facilityDescriptor`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '金融基础设施-元数据信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 批量上报履历
-- ----------------------------
DROP TABLE IF EXISTS `fi_report_history`;
CREATE TABLE `fi_report_history`  (
  `id` int  auto_increment  primary key COMMENT '主键id',
  `branchId` varchar(32)   not null COMMENT '批次号',
  `resp_code`  varchar(32)   NOT NULL COMMENT '返回码',
  `resp_msg`  varchar(50)    NULL COMMENT '返回消息',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  INDEX `idx_branchId`(`branchId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '金融基础设施-批量上报履历' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 批量上报明细
-- ----------------------------
DROP TABLE IF EXISTS `fi_report_detail`;
CREATE TABLE `fi_report_detail`  (
  `id` int  auto_increment  primary key COMMENT '主键id',
  `branchId` varchar(32)   not null COMMENT '批次号',
  `facilityDescriptor`  varchar(32)   NOT NULL COMMENT '全局唯一标识符',
  `resp_code`  varchar(32)   NOT NULL COMMENT '返回码',
  `resp_msg`  varchar(50)    NULL COMMENT '返回消息',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  INDEX `idx_branchId`(`branchId`) USING BTREE,
  INDEX `idx_facilityDescriptor`(`facilityDescriptor`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '金融基础设施-批量上报履历' ROW_FORMAT = Dynamic;


