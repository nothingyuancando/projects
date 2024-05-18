from django.urls import path
from . import views

urlpatterns=[
path('orderlist/',views.order_list),
path('orderadd/',views.order_add),
path('<int:nid>/orderdel/',views.order_del),
]