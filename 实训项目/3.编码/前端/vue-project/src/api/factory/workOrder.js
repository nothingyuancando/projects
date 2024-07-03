import request from '@/utils/request.js';

// 获取工单列表
export const getWorkOrders = () => {
    return request.get('/factory/workorder');
};

// 创建工单
export const createWorkOrder = (data) => {
    return request.post('/factory/workorder', data);
};

// 更新工单状态
export const updateWorkOrderStatus = (workOrderId, status) => {
    return request.put(`/factory/workorder`,  {workorderId:workOrderId,workorderStates:status}  );
};

// 拒绝工单
export const rejectWorkOrder = (workOrderId) => {
    return request.delete(`/api/work-orders/${workOrderId}`);
};
