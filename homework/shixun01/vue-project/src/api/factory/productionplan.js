import axios from 'axios';
import request from '@/utils/request.js';

// 获取生产计划列表
export const fetchProductionPlansFromServer = () => {
    return request.get('/factory/getplan');
};

// 接收生产计划
export const acceptPlanOnServer = (planId) => {
    return request.put(`/factory/plan`,planId);
};

// 拒绝生产计划
export const rejectPlanOnServer = (planId) => {
    return request.post(`/factory/plan`,planId);
};

// 完成生产计划
export const completePlanOnServer = (planId) => {
    return request.post(`/factory/plan`,planId);
};
