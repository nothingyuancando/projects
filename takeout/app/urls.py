from django.urls import path
from . import views

urlpatterns=[
    path('list/', views.food_list),     #菜品列表，搜索
    path('add/', views.food_add),       # 增加
    path('delete/', views.food_del),    #删除
    path('<int:nid>/update/', views.food_update),   #更新
    path('shoplogin/', views.shop_sign),
    path('shopregister/',views.shop_register),
    path('shoplist/', views.shop_list),
    path('shopdelete/', views.shop_del),
    path('<int:nid>/shopupdate/', views.shop_update),
    path('shopadd/', views.shop_add),
    path('<int:nid>/shopfoodlist/', views.shopfood_list),
]



