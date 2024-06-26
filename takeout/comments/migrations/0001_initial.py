# Generated by Django 5.0.4 on 2024-04-16 20:49

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='commentInfo',
            fields=[
                ('commentid', models.AutoField(primary_key=True, serialize=False)),
                ('commentfood', models.FloatField(verbose_name='菜品名称')),
                ('commentuser', models.CharField(max_length=20, unique=True, verbose_name='用户名称')),
                ('commentbody', models.TextField(verbose_name='评论内容')),
            ],
        ),
    ]
