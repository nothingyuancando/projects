// src/api/customer.js
import axiosInstance from '@/utils/request.js';
import {useTokenStore} from "@/stores/token.js";
// 提交订单
export const submitOrderToServer = (order) => {
    return axiosInstance.post('/customer/order', order);
};

// 获取订单列表
export const fetchOrdersFromServer = () => {

    const tokenStore = useTokenStore()

    return axiosInstance.get('/customer/order',);
};

// 获取订单详情
export const fetchOrderDetailsFromServer = (orderId) => {
    return axiosInstance.get(`/customer/order/${orderId}`);
};


// 获取拒单列表
export const fetchRejectedOrdersFromServer = () => {
    return axiosInstance.get('/customer/order');
};

// 修改订单
export const updateOrderOnServer = (order) => {
    return axiosInstance.put(`/customer/order`,order);
};

// 取消订单
export const cancelOrderFromServer = (orderId) => {
    return axiosInstance.delete(`/customer/order/${orderId}`);
};
