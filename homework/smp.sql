/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3306
 Source Schema         : smp

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

 Date: 22/06/2024 19:07:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`  (
  `EquipmentID` int NOT NULL AUTO_INCREMENT COMMENT '设备ID，主键，自增',
  `FactoryID` int NOT NULL COMMENT '工厂ID，关联工厂表',
  `EquipmentName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备名称',
  `SerialNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备序列号，唯一',
  PRIMARY KEY (`EquipmentID`) USING BTREE,
  UNIQUE INDEX `SerialNumber`(`SerialNumber` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of equipment
-- ----------------------------

-- ----------------------------
-- Table structure for equipmentcapacity
-- ----------------------------
DROP TABLE IF EXISTS `equipmentcapacity`;
CREATE TABLE `equipmentcapacity`  (
  `EquipmentCapacityID` int NOT NULL AUTO_INCREMENT COMMENT '设备产能ID，主键，自增',
  `EquipmentID` int NOT NULL COMMENT '设备ID，关联设备表',
  `ProductID` int NOT NULL COMMENT '产品ID，关联产品表',
  `Capacity` int NOT NULL COMMENT '设备产能',
  PRIMARY KEY (`EquipmentCapacityID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备产能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of equipmentcapacity
-- ----------------------------

-- ----------------------------
-- Table structure for factory
-- ----------------------------
DROP TABLE IF EXISTS `factory`;
CREATE TABLE `factory`  (
  `FactoryID` int NOT NULL AUTO_INCREMENT COMMENT '工厂ID，主键，自增',
  `FactoryName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工厂名称，唯一',
  `CreateTime` datetime NOT NULL,
  PRIMARY KEY (`FactoryID`) USING BTREE,
  UNIQUE INDEX `FactoryName`(`FactoryName` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工厂表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of factory
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `OrderID` int NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键，自增',
  `FactoryID` int NOT NULL COMMENT '工厂ID，关联工厂表',
  `ProductID` int NOT NULL COMMENT '产品ID，关联产品表',
  `OrderStatus` enum('未接单','已接单','已拒绝','生产中','已完成') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态',
  `Quantity` int NOT NULL COMMENT '需求产品数量',
  `CreatedDate` datetime NOT NULL COMMENT '订单创建日期',
  `RejectedReason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '拒单备注',
  `CurrentQuantity` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`OrderID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `ProductID` int NOT NULL AUTO_INCREMENT COMMENT '产品ID，主键，自增',
  `FactoryID` int NOT NULL COMMENT '工厂ID，关联工厂表',
  `ProductName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '产品名称',
  `Description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品描述',
  `Consumption` int NOT NULL COMMENT '产品能耗',
  PRIMARY KEY (`ProductID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------

-- ----------------------------
-- Table structure for productionplan
-- ----------------------------
DROP TABLE IF EXISTS `productionplan`;
CREATE TABLE `productionplan`  (
  `PlanID` int NOT NULL AUTO_INCREMENT COMMENT '生产计划ID，主键，自增',
  `OrderID` int NOT NULL COMMENT '订单ID，关联订单表',
  `PlanStatus` enum('未启动','已启动') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '计划状态',
  `StartDate` datetime NOT NULL COMMENT '计划开始日期',
  `EndDate` datetime NOT NULL COMMENT '计划结束日期',
  PRIMARY KEY (`PlanID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '生产计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of productionplan
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `UserID` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自增',
  `Username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名，唯一',
  `Password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `FactoryID` int NOT NULL COMMENT '工厂ID，关联工厂表',
  `UserType` enum('老板','员工') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户类型，老板或员工',
  PRIMARY KEY (`UserID`) USING BTREE,
  UNIQUE INDEX `Username`(`Username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for workorder
-- ----------------------------
DROP TABLE IF EXISTS `workorder`;
CREATE TABLE `workorder`  (
  `WorkOrderID` int NOT NULL AUTO_INCREMENT COMMENT '工单ID，主键，自增',
  `PlanID` int NOT NULL COMMENT '计划ID，关联生产计划表',
  `EquipmentID` int NOT NULL COMMENT '设备ID，关联设备表',
  `WorkOrderStatus` enum('未启动','已启动','已完成') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工单状态',
  `StartDate` datetime NOT NULL COMMENT '工单开始日期',
  `EndDate` datetime NOT NULL COMMENT '工单结束日期',
  `ProcessedQuantity` int NOT NULL COMMENT '加工数量',
  `QualifiedQuantity` int NOT NULL COMMENT '合格数量',
  `ReportedBy` int NOT NULL COMMENT '报工用户ID，关联用户表',
  `ReportDate` datetime NOT NULL COMMENT '报工日期',
  PRIMARY KEY (`WorkOrderID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of workorder
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
