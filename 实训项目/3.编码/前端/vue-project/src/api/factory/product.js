import axios from 'axios';
import request from '@/utils/request.js';

// 获取产品列表
export const fetchProductsFromServer = () => {
    return request.get('/factory/product');
};

// 添加产品
export const addProductToServer = (product) => {
    return request.post('/factory/product', product);
};

// 更新产品
export const updateProductOnServer = (product) => {
    return request.put(`/factory/product`, product);
};

// 删除产品
// export const deleteProductFromServer = ({productId: product_id}) => {
//     return request.delete(`/factory/product`,{productId: product_id});
// };
export const deleteProductFromServer = (productId) => {
    return request.delete(`/factory/product`, { data: productId });
};