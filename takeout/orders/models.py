from django.db import models
from imagekit.models import ProcessedImageField
from imagekit.processors import ResizeToFit
import pymysql
# Create your models here.
from django.contrib.auth.models import User
from app import models as appmodel
from userprofile import models as usermodel
# 引入内置信号
from django.db.models.signals import post_save
from django.dispatch import receiver

class orderInfo(models.Model):
    orderid = models.AutoField(primary_key=True)
    shopname = models.CharField(max_length=50,verbose_name="店铺名称")
    shopaddress = models.CharField(max_length=50,verbose_name="店铺地址")
    username = models.CharField(max_length=50,verbose_name="用户名称")
    useraddresss = models.CharField(max_length=50,verbose_name="用户地址")
    foodname = models.CharField(max_length=50,verbose_name="菜品名称")
    foodprice = models.FloatField(verbose_name="价格")
    amounts= models.IntegerField(verbose_name="数量")
    totalmoney = models.FloatField(verbose_name="总价")
    userId = models.ForeignKey(to="userprofile.userInfo", to_field="userid", on_delete=models.CASCADE)
    foodId =models.ForeignKey(to="app.foodInfo", to_field="foodid", on_delete=models.CASCADE)

