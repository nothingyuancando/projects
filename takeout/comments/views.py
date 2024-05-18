from django.shortcuts import render,redirect
from app import models as appmodel
from userprofile import models as usermodel
from comments import models
from django.db.models import Q
def comment_list(request):
    query_set = models.commentInfo.objects.all()
    query = request.POST.get('search')
    if query:
        query_set = models.commentInfo.objects.filter(Q(commentuser__icontains=query)|Q(commentfood__icontains=query)|Q(commentbody__icontains=query))
    else:
        query_set = models.commentInfo.objects.all()
    return render(request, 'comments/commentlist.html', {'query_set': query_set})
def comment_add(request):
    if request.method =="GET":
        query_set1 = appmodel.foodInfo.objects.all()
        query_set2 = usermodel.userInfo.objects.all()
        return render(request,'comments/commentadd.html',{'query_set1': query_set1,'query_set2':query_set2})
    foodname = request.POST.get('foodname')
    username = request.POST.get('username')
    commentbody = request.POST.get('commentbody')
    foodinfo = appmodel.foodInfo.objects.filter(foodname=foodname).first()
    foodid = foodinfo.foodid
    userinfo = usermodel.userInfo.objects.filter(username=username).first()
    userid = userinfo.userid
    models.commentInfo.objects.create(commentuserid_id=userid,commentfoodid_id=foodid,commentfood=foodname,commentuser=username,commentbody=commentbody)
    return redirect('/comments/commentlist/')

def comment_del(request):
    nid = request.GET.get('nid')
    models.commentInfo.objects.filter(commentid=nid).delete()
    return redirect('/comments/commentlist/')
def comment_update(request,nid):
    if request.method =="GET":
        row_object = models.commentInfo.objects.filter(commentid=nid).first()
        return render(request,'comments/commentupdate.html',{'row_object':row_object})
    comment_body = request.POST.get('commentbody')
    models.commentInfo.objects.filter(commentid=nid).update(commentbody=comment_body)
    return redirect('/comments/commentlist/')
def commentfood_list(request,nid):
    row_object = models.commentInfo.objects.filter(commentfoodid_id=nid).all()
    return render(request, 'comments/commentfoodlist.html', {'query_set': row_object})

