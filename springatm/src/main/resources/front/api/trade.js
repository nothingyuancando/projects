// 查询列表页接口
const getTradeDetailPage = (params) => {
    return $axios({
        url: '/trade/page',
        method: 'get',
        params
    })
}

const getTradePage = (params) => {
    return $axios({
        url: '/trade/page',
        method: 'get',
        params
    })
}

// 查看接口
const queryTradeDetailById = (id) => {
    return $axios({
        url: `/tradeDetail/${id}`,
        method: 'get'
    })
}

// // 取消，派送，完成接口
// const editOrderDetail = (params) => {
//     return $axios({
//         url: '/order',
//         method: 'put',
//         data: { ...params }
//     })
// }
