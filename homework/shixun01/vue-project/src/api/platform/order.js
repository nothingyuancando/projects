import request from '@/utils/request.js';

// 获取订单列表
export const getOrderList = async () => {
    try {
        return await request.get('/platform/getorder');
    } catch (error) {
        throw new Error('获取订单列表失败');
    }
};

// 接单请求
export const acceptOrderRequest = async (order) => {
    try {
        const response = await request.post('/platform/order', {
            orderId: order.order_id,
            orderStates: 2,
        });
        return response.data;
    } catch (error) {
        throw new Error('接单失败');
    }
};

// 拒单请求
export const rejectOrderRequest = async (order) => {
    try {
        const response = await request.post('/platform/order', {
            orderId: order.order_id,
            orderStates: 3,
        });
        return response.data;
    } catch (error) {
        throw new Error('拒单失败');
    }
};

// 完成订单请求
export const completeOrderRequest = async (order) => {
    try {
        const response = await request.post('/platform/order', {
            orderId: order.order_id,
            orderStates: 4,
        });
        return response.data;
    } catch (error) {
        throw new Error('完成订单失败');
    }
};
