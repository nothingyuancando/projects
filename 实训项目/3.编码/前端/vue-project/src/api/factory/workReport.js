import request from '@/utils/request.js';

// 获取工单列表
export const getWorkOrders = () => {
    return request.get('/factory/workorder');
};

// 提交报工数据
export const submitReportData = (report) => {
    return request.post(`/factory/workreport`, {
        workorderId:report.workorderId,		//工单ID
        producingStates:3,	//生产状态//1表示未生产 2表示正在生产 3表示生产结束
        workreportTime:report.workreportTime,	//报工时间
        workHour:report.workHour,		//工时
        processingNumber:report.processingNumber,	//加工数量
        qualifiyNumber:report.qualifiyNumber		//合格数量
    });
};

// 更新工单状态
export const updateWorkOrderStatus = (workOrderId, status) => {
    return request.put(`/work-orders/${workOrderId}`, { status });
};
