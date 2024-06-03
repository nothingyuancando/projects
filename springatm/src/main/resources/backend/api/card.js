function getCardPage (params) {
    return $axios({
        url: '/card/page',
        method: 'get',
        params
    })
}


// 新增
function addCard (params) {
    return $axios({
        url: '/card',
        method: 'post',
        data: { ...params }
    })
}

// 修改
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
        url: `/card/${id}`,
        method: 'get'
    })
}
const deleteCard = (ids) => {
    return $axios({
        url: '/card',
        method: 'delete',
        params: { ids }
    })
}

function enableOrDisableCard (params) {
    return $axios({
        url: '/card/switch',
        method: 'put',
        data: { ...params }
    })
}
