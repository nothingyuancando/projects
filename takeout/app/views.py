from django.shortcuts import render,redirect
from app import models
from app.models import shopInfo
from app.models import foodInfo
from django.db.models import Q
# Create your views here.
def food_list(request):
    query_set = models.foodInfo.objects.all()
    query = request.POST.get('search') #是否进行搜索
    if query:                          #进行模糊搜索使用Q功能
        query_set = models.foodInfo.objects.filter(
            Q(foodname__icontains=query)| Q(describ__icontains=query)|Q(foodprice__icontains=query)
        )                   # 存下搜索结果
    else:
        query_set = models.foodInfo.objects.all()
    return render(request,'app/foodlist.html',{'query_set':query_set})


def food_add(request):
    if request.method =="GET":                  #显示菜品信息
        query_set = models.shopInfo.objects.all()
        return render(request,'app/foodadd.html',{'query_set':query_set})      #返回前端页面和请求信息
    foodtupian = request.FILES.get('foodtupian')                      #显示信息
    food_name = request.POST.get('foodname')
    food_price = request.POST.get('foodprice')
    food_price = float(food_price)
    descr = request.POST.get('describle')
    shopname = request.POST.get('shopname')
    shopinfo = models.shopInfo.objects.filter(shopname=shopname).first()      #查找菜品商家信息
    shopid = shopinfo.shopid
    shopid = int(shopid)
    models.foodInfo.objects.create(foodname = food_name,foodprice = food_price,describ = descr,belongshop_id = shopid,tupian=foodtupian)#添加菜品
    return redirect('/app/add/')            #更新页面



def food_del(request):
    nid = request.GET.get('nid')    #查找菜品ID
    models.foodInfo.objects.filter(foodid=nid).delete() #删除菜品
    return redirect('/app/list/')       #更新菜品列表

def food_update(request,nid):
    if request.method =="GET":          #显示当前菜品信息
        row_object = models.foodInfo.objects.filter(foodid=nid).first()
        return render(request,'app/foodupdate.html',{'row_object':row_object})

    food_name = request.POST.get('foodname')        #保存前端传入的数据
    food_price = request.POST.get('foodprice')
    food_price = float(food_price)
    descr = request.POST.get('describle')

    models.foodInfo.objects.filter(foodid=nid).update(foodname=food_name, foodprice=food_price, describ=descr)  #修改菜品
    return redirect('/app/list/')

def shop_sign(request):
    if request.method =="GET":
        return render(request,'userprofile/shoplogin.html')
    shop_id = request.POST.get('shopid')
    shoppwd = request.POST.get('password')
    if models.shopInfo.objects.filter(shopid=shop_id,password=shoppwd).exists():
        return redirect('/app/list/')
    else:
        return render(request, 'userprofile/shoplogin.html')


def shop_register(request):

    if request.method =="GET":
        return render(request,'userprofile/shopregister.html')
    shop_id = request.POST.get('shopid')
    shop_name = request.POST.get('shopname')
    shoppassword = request.POST.get('shoppassword')
    shopaddress = request.POST.get('shopaddress')
    shopworktime = request.POST.get('worktimes')
    shopphone = request.POST.get('shopphone')
    models.shopInfo.objects.create(shopid=shop_id,shopname=shop_name,password=shoppassword,shopaddress=shopaddress,shopphone=shopphone,worktime=shopworktime)
    return redirect('/app/list/')


def shop_list(request):
    query_set = models.shopInfo.objects.all()
    return render(request,'app/shoplist.html',{'query_set':query_set})

def shop_add(request):
    if request.method =="GET":
        return render(request,'app/shopadd.html')

    shop_name = request.POST.get('shopname')
    shopaddress = request.POST.get('shopaddress')
    shopphone = request.POST.get('shopphone')
    shopworktime = request.POST.get('shoptime')

    models.shopInfo.objects.create(shopname=shop_name,shopaddress=shopaddress,shopphone=shopphone,worktime=shopworktime)
    return redirect('/app/shopadd/')

def shop_del(request):
    nid = request.GET.get('nid')
    models.shopInfo.objects.filter(shopid=nid).delete()
    return redirect('/app/shoplist/')

def shop_update(request,nid):
    if request.method =="GET":
        row_object = models.shopInfo.objects.filter(shopid=nid).first()
        return render(request,'app/shopupdate.html',{'row_object':row_object})
    shop_name = request.POST.get('shopname')
    shop_address = request.POST.get('shopaddress')
    shop_phone = request.POST.get('shopphone')
    worktime = request.POST.get('worktime')
    models.shopInfo.objects.filter(shopid=nid).update( shopname=shop_name, shopaddress=shop_address, shopphone=shop_phone, worktime=worktime)
    return redirect('/app/shoplist/')

def shopfood_list(request,nid):
    row_object = models.foodInfo.objects.filter(belongshop_id=nid).all()
    return render(request, 'app/shopfoodlist.html', {'query_set': row_object})

