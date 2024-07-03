import axios from 'axios';
import request from '@/utils/request.js';

// 获取生产计划列表
// export const getPlans = () => {
//     return request.get('/api/plans');
// };
//
// // 获取生产订单列表
// export const getProductionOrders = () => {
//     return request.get('/api/productionOrders');
// };
//
// // 获取工厂列表
// export const getFactories = () => {
//     return request.get('/api/factories');
// };
//
// // 获取订单产品列表
// export const getOrderProducts = (orderId) => {
//     return request.get(`/api/orders/${orderId}/products`);
// };
//
// // 创建生产计划
// export const createPlanRequest = (planData) => {
//     return request.post('/api/plans', planData);
// };
//
// // 启动生产计划
// export const startPlanRequest = (planId) => {
//     return request.post(`/api/plans/${planId}/start`);
// };
//
// // 删除生产计划
// export const deletePlanRequest = (planId) => {
//     return request.delete(`/api/plans/${planId}`);
// };
export const getProductionPlans = () => {
    return request.get('/production-1111plans');
};

export const getProductionOrders = () => {
    return request.get('/platform/getorder');
};

export const createProductionPlan = (plan) => {
    return request.post('/platform/plan', plan);
};

export const updateProductionPlanStatus = (data) => {
    return request.put(`/production-plans/${data.planId}/status`, data);
};

export const deleteProductionPlan = (planId) => {
    return request.delete(`/production-plans/${planId}`);
};

