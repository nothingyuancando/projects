from django.db import models
from imagekit.models import ProcessedImageField
from imagekit.processors import ResizeToFit
from userprofile.models import userInfo
from app.models import foodInfo
import pymysql
from PIL import Image
# Create your models here.

class commentInfo(models.Model):
    commentid = models.AutoField(primary_key=True)
    commentfood = models.CharField(max_length=20,verbose_name="菜品名称")
    commentuser = models.CharField(max_length=20, verbose_name="用户名称")
    commentbody = models.TextField(verbose_name="评论内容")
    commentuserid = models.ForeignKey(to="userprofile.userInfo",to_field="userid",on_delete=models.CASCADE)
    commentfoodid = models.ForeignKey(to="app.foodInfo",to_field="foodid",on_delete=models.CASCADE)


