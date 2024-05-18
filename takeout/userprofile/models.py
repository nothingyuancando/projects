from django.db import models
from imagekit.models import ProcessedImageField
from imagekit.processors import ResizeToFit
import pymysql

# Create your models here.
from django.contrib.auth.models import User
# 引入内置信号
from django.db.models.signals import post_save
# 引入信号接收器的装饰器
from django.dispatch import receiver


class adminiInfo(models.Model):
    # 2.定义字段 属性
    adminId = models.AutoField(primary_key=True, default=None)
    password = models.CharField(max_length=16, blank=False, verbose_name="密码")

# 创建用户信息类
class userInfo(models.Model):
    userid = models.AutoField(primary_key=True, default=None)
    username = models.CharField(max_length=16, verbose_name="用户名",unique=True)
    userphone = models.CharField(max_length=16, verbose_name="电话")
    useraddress = models.CharField(max_length=50, verbose_name="地址")
    password = models.CharField(max_length=16, blank=False, verbose_name="密码")
    money = models.FloatField(verbose_name="余额",default=100)










