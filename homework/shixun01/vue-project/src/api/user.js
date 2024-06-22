import request from '@/utils/request.js'

export const userRegisterServer = (registerData) =>{

    const params = new URLSearchParams()
    for (let key in registerData) {
        params.append(key, registerData[key]);
    }


    return request.post('/user/register', params);
}

export const userLoginServer = (loginData) =>{

    const params = new URLSearchParams()
    for (let key in loginData) {
        params.append(key, loginData[key]);
    }
    return request.post('/user/login', params);
}

