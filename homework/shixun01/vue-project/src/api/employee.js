import request from '@/utils/request.js'
import {useTokenStore} from '@/stores/token.js'
export const employeeListService =()=>{



return  request.get('../employee/',)

}
export const employeeAddService =(emoloyeeData)=>{

    return  request.post('../employee/',emoloyeeData)

}