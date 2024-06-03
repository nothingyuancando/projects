const getCardList = (number) => {
    return $axios({
        url: '/card/list?number='+number,
        method: 'get',

    })
}

// 修改---启用禁用接口
function enableOrDisableEmployee (params) {
    return $axios({
        url: '/user',
        method: 'put',
        data: { ...params }
    })
}

// 新增---添加员工
function addEmployee (params) {
    return $axios({
        url: '/user',
        method: 'post',
        data: { ...params }
    })
}

// 修改---添加员工
function editEmployee (params) {
    return $axios({
        url: '/user',
        method: 'put',
        data: { ...params }
    })
}

// 修改页面反查详情接口
function queryEmployeeById (id) {
    return $axios({
        url: `/employee/${id}`,
        method: 'get'
    })
}