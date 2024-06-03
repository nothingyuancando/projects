function loginApi(data) {
  return $axios({
    'url': '/card/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/card/logout',
    'method': 'post',
  })
}
