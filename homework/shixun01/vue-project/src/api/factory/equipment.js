import axios from 'axios';
import request from '@/utils/request.js';


// 获取设备列表
export const fetchDevicesFromServer = () => {
    return request.get('/factory/equipment');
};

// 添加设备
export const addDeviceToServer = (device) => {
    return request.post('/factory/equipment', device);
};

// 更新设备
export const updateDeviceOnServer = (device) => {
    return request.put(`/factory/equipment`, device);
};

// 删除设备
export const deleteDeviceFromServer = (deviceId) => {
    return request.delete(`/factory/equipment`,{ data: {equipmentId: deviceId} });
};

// 获取设备关联产品
export const fetchDeviceProductsFromServer = (deviceId) => {
    return request.get(`/factory/equipment/${deviceId}/products`);
};

// 给设备添加产品
export const addProductToDeviceOnServer = (deviceId, productName) => {
    return request.post(`/factory/equipment/${deviceId}/products`, { name: productName });
};

// 删除设备上的产品
export const deleteProductFromDeviceOnServer = (productId) => {
    return request.delete(`/factory/products/${productId}`);
};

export const   linkProductToDeviceOnServer = (product) => {
    return request.post(`/factory/relation`, product);
}