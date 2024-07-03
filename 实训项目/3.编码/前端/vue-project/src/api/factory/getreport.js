import request from '@/utils/request.js';

// 获取报工记录列表
export const getWorkReportList = () => {
    return request.get('/factory/order');
};

