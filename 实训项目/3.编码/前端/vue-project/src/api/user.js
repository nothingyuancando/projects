import request from '@/utils/request.js';


export const userRegisterServer = (registerData) => {
    return request.post('/register', registerData);
};

export const userLoginServer = (loginData) => {
    return request.post('/login', loginData);
};

