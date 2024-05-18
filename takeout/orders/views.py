from django.shortcuts import render,redirect
from app import models as appmodel
from userprofile import models as usermodel
from orders import models
from app.models import shopInfo,foodInfo
from userprofile.models import userInfo
from orders.models import orderInfo
from decimal import Decimal
def order_list(request):
    query_set = models.orderInfo.objects.all()
    return render(request, 'orders/orderlist.html', {'query_set': query_set})

def order_add(request):
    if request.method =="GET":
        query_set1 = appmodel.foodInfo.objects.all()            #外键找到菜品信息
        query_set2 = usermodel.userInfo.objects.all()           #外键找到以哦那个胡信息
        return render(request,'orders/orderadd.html',{'query_set1': query_set1,'query_set2':query_set2})
    foodname = request.POST.get('foodname')                     #选择菜品
    sums = request.POST.get('sums')                             #选择数量
    sums = int(sums)
    foodinfo = appmodel.foodInfo.objects.filter(foodname=foodname).first()      #添加菜品ID
    foodprice = foodinfo.foodprice
    bel = foodinfo.belongshop_id

    shopinfo = appmodel.shopInfo.objects.filter(shopid=bel).first()              #添加店铺ID
    shopaddress = shopinfo.shopaddress
    shopname = shopinfo.shopname
    gm = sums*foodprice                                  #更新商家营业额
    getmoneys = shopinfo.getmoney + sums*foodprice
    appmodel.shopInfo.objects.filter(shopid=bel).update(getmoney=getmoneys)#更新商家信息

    username = request.POST.get('username')
    userinfo = usermodel.userInfo.objects.filter(username=username).first()
    usermoney = userinfo.money - sums*foodprice         #更新用户余额
    foodid = foodinfo.foodid

    usermodel.userInfo.objects.filter(username=username).update(money=usermoney)
    useraddress = userinfo.useraddress

    userid = userinfo.userid
    #添加订单
    models.orderInfo.objects.create(userId_id=userid,foodId_id=foodid,amounts=sums,foodname=foodname,foodprice=foodprice,shopname=shopname,username=username,useraddresss=useraddress,shopaddress=shopaddress,totalmoney=gm)
    return redirect('/orders/orderlist/')

def order_del(request):
    nid = request.GET.get('nid')
    models.orderInfo.objects.filter(orderid=nid).delete()
    return redirect('/orders/orderlist/')
