from django.urls import path
from . import views

urlpatterns=[
path('commentlist/',views.comment_list),
    path('commentadd/',views.comment_add),
    path('commentdel/',views.comment_del),
    path('<int:nid>/commentupdate/',views.comment_update),
    path('<int:nid>/commentfoodlist/',views.commentfood_list),
]