import axios from 'axios';
import request from '@/utils/request.js';

export const getFactoryOverviewData = () => {
    return request.get('/factory/view');
};