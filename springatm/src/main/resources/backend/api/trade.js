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

