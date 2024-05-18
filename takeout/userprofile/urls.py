from django.urls import path
from . import views

urlpatterns=[
path('userlist/',views.user_list),
    path('useradd/',views.user_add),
    path('userdel/',views.user_del),
    path('<int:nid>/userupdate/',views.user_update),
    path('userdel/',views.user_del),
path('adminls/',views.admin_sign),
path('userregiser/',views.user_register),
path('adminregiser/',views.admin_register),
]