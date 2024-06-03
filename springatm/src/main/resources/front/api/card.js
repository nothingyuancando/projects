function getCardPage (params) {
    return $axios({
        url: '/card/list?number='+params,
        method: 'get',
    })
}

// 修改---启用禁用接口
function enableOrDisableUser (params) {
    return $axios({
        url: '/card',
        method: 'put',
        data: { ...params }
    })
}



// 修改---添加员工
function editCard (params) {
    return $axios({
        url: '/card',
        method: 'put',
        data: { ...params }
    })
}

// 修改页面反查详情接口
function queryCardById (id) {
    return $axios({
        url: `/card/number/${id}`,
        method: 'get'
    })
}

function trade(){

    return $axios({
        url: `/card/trade`,
        method: 'post'
    })

}

// 修改---添加员工
function tradeCard (params) {
    return $axios({
        url: '/card/trade',
        method: 'put',
        data: { ...params }
    })
}