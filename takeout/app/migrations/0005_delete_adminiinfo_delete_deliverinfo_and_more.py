# Generated by Django 5.0.4 on 2024-04-15 15:31

import imagekit.models.fields
from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0004_foodinfo_tupian'),
    ]

    operations = [
        migrations.DeleteModel(
            name='adminiInfo',
        ),
        migrations.DeleteModel(
            name='deliverInfo',
        ),
        migrations.RemoveField(
            model_name='orderinfo',
            name='foodid',
        ),
        migrations.DeleteModel(
            name='userInfo',
        ),
        migrations.AlterField(
            model_name='foodinfo',
            name='tupian',
            field=imagekit.models.fields.ProcessedImageField(upload_to='article/%Y%m%d'),
        ),
        migrations.DeleteModel(
            name='orderInfo',
        ),
    ]
