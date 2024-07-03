import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'https://api.example.com', // 替换为你的实际API URL
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
    }
});

export const fetchPlans = async () => {
    const response = await apiClient.get('/plans');
    return response.data;
};

export const fetchFactories = async () => {
    const response = await apiClient.get('/factories');
    return response.data;
};

export const dispatchPlanToFactory = async (planId, factoryId) => {
    const response = await apiClient.post('/dispatch', { planId, factoryId });
    return response.data;
};

export const fetchDispatchList = async () => {
    const response = await apiClient.get('/dispatches');
    return response.data;
};
