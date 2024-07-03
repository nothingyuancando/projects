import axios from 'axios';

const instance = axios.create({
    baseURL: '/customer', // 基础URL，可以根据需要修改
    timeout: 10000, // 请求超时时间
});

export const fetchRejectedOrdersFromServer = () => {
    return instance.get('/rejected-orders');
};

export const updateOrderOnServer = (order) => {
    return instance.put(`/orders/${order.orderId}`, order);
};

export const cancelOrderFromServer = (orderId) => {
    return instance.delete(`/orders/${orderId}`);
};

export const fetchOrdersFromServer = () => {
    return instance.get('/orders');
};

export const fetchOrderDetailsFromServer = (orderId) => {
    return instance.get(`/orders/${orderId}`);
};