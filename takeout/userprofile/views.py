from django.shortcuts import render,redirect
from app.models import shopInfo
from django.contrib.auth import authenticate, login
from django.http import HttpResponse
from .forms import UserLoginForm, UserRegisterForm
# Create your views here.
from django.contrib.auth import authenticate, login,logout
from django.http import HttpResponse
from userprofile import models

# Create your views here.

def user_sign(request):
    if request.method =="GET":
        return render(request,'userprofile/userlogin.html')
    shop_id = request.POST.get('userid')
    shoppwd = request.POST.get('password')
    if models.userInfo.objects.filter(userid=shop_id,password=shoppwd).exists():
        return redirect('/app/buy/')
    else:
        return render(request, 'userprofile/userlogin.html')


def user_register(request):

    if request.method =="GET":
        return render(request,'userprofile/userregister.html')
    shop_id = request.POST.get('userid')
    shop_name = request.POST.get('username')
    shoppassword = request.POST.get('userpassword')
    shopaddress = request.POST.get('useraddress')
    shopphone = request.POST.get('userphone')
    models.userInfo.objects.create(userid=shop_id,username=shop_name,password=shoppassword,useraddress=shopaddress,userphone=shopphone)
    return redirect('/app/buy/')

def admin_sign(request):
    if request.method =="GET":
        return render(request, 'userprofile/start.html')
    shop_id = request.POST.get('adminid')
    shoppwd = request.POST.get('password')
    if models.adminiInfo.objects.filter(adminId=shop_id,password=shoppwd).exists():
        return redirect('/app/list/')
    else:
        return render(request, 'userprofile/start.html')


def admin_register(request):

    if request.method =="GET":
        return render(request,'userprofile/adminrgs.html')
    adminid = request.POST.get('adminid')
    password = request.POST.get('adminpwd')
    models.adminiInfo.objects.create(adminId=adminid,password=password)
    return redirect('/app/list/')


def user_list(request):
    query_set = models.userInfo.objects.all()
    return render(request,'userprofile/userlist.html',{'query_set':query_set})

def user_add(request):
    if request.method =="GET":
        return render(request,'userprofile/useradd.html')
    username = request.POST.get('username')
    password = request.POST.get('userpwd')
    useraddress = request.POST.get('useraddress')
    userphone = request.POST.get('userphone')
    models.userInfo.objects.create( username=username, password=password, useraddress=useraddress, userphone=userphone)
    return redirect('/userprofile/useradd/')

def user_del(request):
    nid = request.GET.get('nid')
    models.userInfo.objects.filter(userid=nid).delete()
    return redirect('/userprofile/userlist/')

def user_update(request,nid):
    if request.method =="GET":
        row_object = models.userInfo.objects.filter(userid=nid).first()
        return render(request,'userprofile/userupdate.html',{'row_object':row_object})
    user_name = request.POST.get('username')
    userphone = request.POST.get('userphone')
    useraddress = request.POST.get('useraddress')
    money = request.POST.get('money')
    money = float(money)


    models.userInfo.objects.filter(userid=nid).update(username=user_name,userphone=userphone,useraddress=useraddress,money=money)
    return redirect('/userprofile/userlist/')

