from django.db import models
from imagekit.models import ProcessedImageField
from imagekit.processors import ResizeToFit
import pymysql
from PIL import Image
# Create your models here.

class shopInfo(models.Model):
    shopid = models.AutoField(primary_key=True)
    shopname = models.CharField(max_length=16, verbose_name="店铺名称",unique=True)
    shopphone = models.CharField(max_length=16, verbose_name="电话")
    shopaddress = models.CharField(max_length=16, verbose_name="地址")
    password = models.CharField(max_length=16, blank=False, verbose_name="密码")
    worktime = models.CharField(max_length=40,)
    getmoney = models.FloatField(verbose_name="营业额",default=100)


class foodInfo(models.Model):
    foodid = models.AutoField(primary_key=True, default=None)
    foodprice = models.FloatField(verbose_name="价格")
    foodname = models.CharField(max_length=20,verbose_name="名称",unique=True)
    belongshop = models.ForeignKey(to="shopInfo",to_field="shopid",on_delete=models.CASCADE)
    describ = models.TextField(verbose_name="菜品描述")
    tupian = ProcessedImageField(
        upload_to='food/%Y%m%d',
        processors=[ResizeToFit(width=400)],
        format='JPEG',
        options={'quality': 100},
    )





